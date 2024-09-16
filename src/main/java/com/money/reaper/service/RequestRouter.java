package com.money.reaper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.model.Transaction;
import com.money.reaper.repository.TransactionRepository;
import com.money.reaper.service.acquirer.AcquirerManagerFactory;
import com.money.reaper.service.acquirer.UPIGatewayProcessor;
import com.money.reaper.util.TransactionStatus;

import io.micrometer.common.util.StringUtils;

@Service
public class RequestRouter {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AcquirerManagerFactory acquirerManagerFactory;

	@Autowired
	private UPIGatewayProcessor upiGatewayProcessor;

	private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

	public Transaction route(Transaction transaction) {
		if (transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		String acquirer;
		try {
			acquirer = acquirerManagerFactory.getRandomAcquirer();
		} catch (Exception e) {
			logger.error("Failed to get random acquirer", e);
			setTransactionError(transaction);
			return saveAndReturnTransaction(transaction);
		}
		if (StringUtils.isEmpty(acquirer)) {
			logger.warn("No active acquirer");
			setTransactionError(transaction);
			return saveAndReturnTransaction(transaction);
		}
		try {
			if (acquirer.equalsIgnoreCase("UPI_GATEWAY")) {
				transaction = upiGatewayProcessor.initiatePayment(transaction);
			}
		} catch (Exception e) {
			logger.error("Payment initiation failed", e);
			setTransactionError(transaction);
		}
		return saveAndReturnTransaction(transaction);
	}

	private void setTransactionError(Transaction transaction) {
		transaction.setStatus(TransactionStatus.ERROR);
		transaction.setPgResponseCode(TransactionStatus.ERROR.getCode());
		transaction.setPgResponseMessage(TransactionStatus.ERROR.getDisplayName());
	}

	private Transaction saveAndReturnTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
		return transaction;
	}

}
