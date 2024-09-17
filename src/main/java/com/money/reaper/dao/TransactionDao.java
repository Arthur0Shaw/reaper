package com.money.reaper.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.dto.DashboardRequest;
import com.money.reaper.dto.DashboardResponseData;
import com.money.reaper.model.Transaction;
import com.money.reaper.repository.TransactionRepository;

@Service
public class TransactionDao {

	@Autowired
	private TransactionRepository transactionRepository;

	public List<Transaction> getAllTransactions() {
		return transactionRepository.findAllByOrderByCreatedAtDesc();
	}

	public List<Transaction> getTransactionsByCreatedAt(LocalDateTime startDate, LocalDateTime endDate) {
		return transactionRepository.findByCreatedAtBetween(startDate, endDate);
	}

	public List<Transaction> getTransactionsByDateIndex(String startDate, String endDate) {
		return transactionRepository.findByDateIndexBetween(startDate, endDate);
	}

	public List<Transaction> getTransactionsByFilters(String status, String id, String acquirerReferenceId,
			String merchantOrderId) {
		return transactionRepository.findByStatusAndIdAndAcquirerReferenceIdAndMerchantOrderId(status, id,
				acquirerReferenceId, merchantOrderId);
	}

	public DashboardResponseData getDashboardData(DashboardRequest request) {
		DashboardResponseData result = null;
		if (request.getStatus() != null) {
			result = transactionRepository.findAggregatedTransactionsByStatus(request.getStatus(),
					request.getDateIndexFrom(), request.getDateIndexTo());
		} else {
			result = transactionRepository.findAllAggregatedTransactions(request.getDateIndexFrom(),
					request.getDateIndexTo());
		}
		return result;
	}
}
