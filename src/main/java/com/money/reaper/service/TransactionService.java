package com.money.reaper.service;

import org.springframework.stereotype.Service;

import com.money.reaper.dto.InitiateTransactionRequest;
import com.money.reaper.model.Transaction;

import jakarta.validation.Valid;

@Service
public class TransactionService {

	public Transaction initiateNewTransaction(@Valid InitiateTransactionRequest request) {
		try {

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
}
