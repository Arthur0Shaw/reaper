package com.money.reaper.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.money.reaper.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/transaction")
@Validated
public class TransactionController {
	
	@Autowired
	private TransactionDao transactionDao;

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/initiateTransaction")
	public ResponseEntity<?> initiateTransaction(@Valid @RequestBody InitiateTransactionRequest request) {
		try {
			Transaction transaction = transactionService.initiateNewTransaction(request);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Transaction initiated successfully");
			response.put("transaction", transaction);
			return ResponseEntity.ok(response); // Return success with transaction object
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", "An unexpected error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/report")
	public ResponseEntity<?> getTransactionsDetails(@RequestBody TransactionReportRequest txnReportRequest) {
		try {
			List<Transaction> transactions;
			if (txnReportRequest.getStartDate() != null && txnReportRequest.getEndDate() != null) {
				transactions = transactionDao.getTransactionsByCreatedAt(txnReportRequest.getStartDate(),
						txnReportRequest.getEndDate());
			} else if (txnReportRequest.getDateIndexStart() != null && txnReportRequest.getDateIndexEnd() != null) {
				transactions = transactionDao.getTransactionsByDateIndex(txnReportRequest.getDateIndexStart(),
						txnReportRequest.getDateIndexEnd());
			} else {
				throw new IllegalArgumentException("At least one filter criteria must be provided.");
			}
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Transaction report fetched successfully");
			response.put("transactions", transactions);
			return ResponseEntity.ok(response); // Return success with transaction list
		} catch (IllegalArgumentException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", "An unexpected error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}
}