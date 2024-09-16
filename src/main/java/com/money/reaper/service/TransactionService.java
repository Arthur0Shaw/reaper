package com.money.reaper.service;

import org.springframework.stereotype.Service;

import com.money.reaper.model.Transaction;
import com.money.reaper.repository.TransactionRepository;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllByOrderByCreatedAtAsc();
    }

    public List<Transaction> getTransactionsByCreatedAt(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByCreatedAtBetween(startDate, endDate);
    }

    public List<Transaction> getTransactionsByDateIndex(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByDateIndexBetween(startDate, endDate);
    }

    public List<Transaction> getTransactionsByFilters(String status, String id, String acquirerReferenceId, String merchantOrderId) {
        return transactionRepository.findByStatusAndIdAndAcquirerReferenceIdAndMerchantOrderId(
                status, id, acquirerReferenceId, merchantOrderId);
    }
}

