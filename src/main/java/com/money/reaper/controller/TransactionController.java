package com.money.reaper.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.reaper.dao.TransactionDao;
import com.money.reaper.dto.InitiateTransactionRequest;
import com.money.reaper.dto.TransactionReportRequest;
import com.money.reaper.model.Transaction;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/transaction")
@Validated
public class TransactionController {

	private final TransactionDao transactionDao;

	public TransactionController(TransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}

	@PostMapping("/initiateTransaction")
	public ResponseEntity<String> initiateTransaction(@Valid @RequestBody InitiateTransactionRequest request) {
		// Business logic here
		return ResponseEntity.ok("Transaction initiated successfully");
	}

	@PostMapping("/report")
	public ResponseEntity<List<Transaction>> getTransactionsDeatils(
			@RequestBody TransactionReportRequest txnReportrequest) {
		List<Transaction> transactions;
		if (txnReportrequest.getStartDate() != null && txnReportrequest.getStartDate() != null) {
			transactions = transactionDao.getTransactionsByCreatedAt(txnReportrequest.getStartDate(),
					txnReportrequest.getStartDate());
		} else if (txnReportrequest.getDateIndexStart() != null && txnReportrequest.getDateIndexEnd() != null) {
			transactions = transactionDao.getTransactionsByDateIndex(txnReportrequest.getDateIndexStart(),
					txnReportrequest.getDateIndexEnd());
		} else {
			throw new IllegalArgumentException("At least one filter criteria must be provided.");
		}
		return ResponseEntity.ok(transactions);
	}
}