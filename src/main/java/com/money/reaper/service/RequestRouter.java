package com.money.reaper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.model.Transaction;
import com.money.reaper.repository.TransactionRepository;
import com.money.reaper.service.acquirer.AcquirerManagerFactory;
import com.money.reaper.service.acquirer.upigateway.UPIGatewayProcessor;
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

	public Transaction routeNewTransaction(Transaction transaction) {
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
			transaction.setAcquirer(acquirer);
			if (acquirer.equalsIgnoreCase("UPI_GATEWAY")) {
				transaction = upiGatewayProcessor.initiatePayment(transaction);
			}
		} catch (Exception e) {
			logger.error("Payment initiation failed", e);
			setTransactionError(transaction);
		}
		return saveAndReturnTransaction(transaction);
	}

	public Transaction routeTransactionStatusEnquiry(Transaction transaction) {
		try {
			if (transaction == null) {
				throw new IllegalArgumentException("Transaction cannot be null");
			}
			String acquirer = transaction.getAcquirer();
			if (acquirer.equalsIgnoreCase("UPI_GATEWAY")) {
				transaction = upiGatewayProcessor.initiatePaymentStatus(transaction);
			}
		} catch (Exception e) {
			logger.error("Transaction status enquiry failed", e);
		}
		return saveAndReturnTransaction(transaction);
	}

	public void handleWebhook(String webhookPayload, String acquirer) {
		try {
			if (acquirer.equalsIgnoreCase("UPI_GATEWAY")) {
				Transaction	transaction = upiGatewayProcessor.handleWebhook(webhookPayload);
				transactionRepository.save(transaction);
			}
		} catch (Exception e) {
			logger.error("Transaction status enquiry failed", e);
		}
	}

	private void setTransactionError(Transaction transaction) {
		transaction.setStatus(TransactionStatus.FAILURE);
		transaction.setPgResponseCode(TransactionStatus.FAILURE.getCode());
		transaction.setPgResponseMessage(TransactionStatus.FAILURE.getDisplayName());
	}

	private Transaction saveAndReturnTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
		return transaction;
	}

}
