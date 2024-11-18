package com.money.reaper.service.acquirer.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.money.reaper.model.Transaction;
import com.money.reaper.util.DateTimeCreator;
import com.money.reaper.util.TransactionStatus;

@Service
public class WebStatusProcessor {

	private static Map<String, String> transactionStatusMap = new HashMap<String, String>();

	private static final Logger logger = LoggerFactory.getLogger(WebStatusProcessor.class);

	public Transaction initiatePaymentStatus(Transaction transaction) {
		try {
			if (transactionStatusMap.containsKey(transaction.getPgOrderId())) {
				String transactionDetails = transactionStatusMap.get(transaction.getPgOrderId());
				if (transaction.getAmount().equals(transactionDetails.split("|")[0])) {
					transaction.setUpdatedAt(DateTimeCreator.getDateTime());
					transaction.setAcquirerReferenceId(String.valueOf(transactionDetails.split("|")[2]));
					transaction.setStatus(TransactionStatus.SUCCESS);
					transaction.setPgResponseCode(TransactionStatus.SUCCESS.getCode());
					transaction.setPgResponseMessage(TransactionStatus.SUCCESS.getDisplayName());
				} else {
					logger.error(
							"Amount mismatch in request and response, amount in request is " + transaction.getAmount()
									+ " while amount in response is: " + transactionDetails.split("|")[0]);
					transaction.setAcquirerReferenceId(String.valueOf(transactionDetails.split("|")[2]));
					transaction.setUpdatedAt(DateTimeCreator.getDateTime());
					transaction.setStatus(TransactionStatus.FAILURE);
					transaction.setPgResponseCode(TransactionStatus.FAILURE.getCode());
					transaction.setPgResponseMessage("Amount mismatch, amount in request: " + transaction.getAmount()
							+ " while amount in response: " + String.valueOf(transactionDetails.split("|")[0]));
				}
			}
		} catch (Exception e) {
			logger.error("Exception ", e);
		}
		return transaction;
	}

}
