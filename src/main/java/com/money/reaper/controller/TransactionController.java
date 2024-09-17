package com.money.reaper.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
import com.money.reaper.dto.DashboardRequest;
import com.money.reaper.dto.InitiateTransactionRequest;
import com.money.reaper.dto.InitiateTransactionResponse;
import com.money.reaper.dto.StatusEnquiryRequest;
import com.money.reaper.dto.TransactionReportRequest;
import com.money.reaper.model.Transaction;
import com.money.reaper.service.TransactionService;

import jakarta.servlet.http.HttpServletRequest;
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
	public ResponseEntity<?> initiateTransaction(@Valid @RequestBody InitiateTransactionRequest request,
			HttpServletRequest httpServletRequest) {
		try {
			String ipAddress = httpServletRequest.getRemoteAddr();
			InitiateTransactionResponse transactionResponse = transactionService.initiateNewTransaction(request,
					ipAddress);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Transaction initiated successfully");
			response.put("transaction", transactionResponse);
			return ResponseEntity.ok(response); // Return success with transaction object
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", "An unexpected error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/upiGatewayWebhook")
	public ResponseEntity<?> upiGatewayWebhook(HttpServletRequest httpServletRequest) {
		try {
			StringBuilder payloadBuilder = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(httpServletRequest.getInputStream(), StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					payloadBuilder.append(line);
				}
			}
			String payload = payloadBuilder.toString();
			transactionService.handleAcquirerWebhook(payload, "UPI_GATEWAY");
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/transactionStatus")
	public ResponseEntity<?> transactionStatus(@Valid @RequestBody StatusEnquiryRequest request,
			HttpServletRequest httpServletRequest) {
		try {
			String ipAddress = httpServletRequest.getRemoteAddr();
			InitiateTransactionResponse transactionStatusResponse = transactionService.transactionStatusUpdate(request,
					ipAddress);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "success");
			response.put("transaction", transactionStatusResponse);
			return ResponseEntity.ok(response);
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
			if (txnReportRequest.getDateIndexFrom() != null && txnReportRequest.getDateIndexTo() != null) {
				transactions = transactionDao.getTransactionsByDateIndex(txnReportRequest.getDateIndexFrom(),
						txnReportRequest.getDateIndexTo());
			} else {
				throw new IllegalArgumentException("Date index filter criteria must be provided.");
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

	@PostMapping("/dashboard")
	public ResponseEntity<?> getTransactionDashboardData(@RequestBody DashboardRequest dasbhoardRequest) {
		try {
			return ResponseEntity.ok(transactionDao.getDashboardData(dasbhoardRequest));
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