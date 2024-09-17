package com.money.reaper.service.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.money.reaper.model.Transaction;
import com.money.reaper.repository.TransactionRepository;
import com.money.reaper.service.RequestRouter;
import com.money.reaper.util.DateTimeCreator;
import com.money.reaper.util.TransactionStatus;

@Service
public class TransactionSchedulerService {

	@Autowired
	private RequestRouter requestRouter;

	@Autowired
	private TransactionRepository transactionRepository;

	@Scheduled(cron = "0 */15 * * * *")
	public void fetchPendingTransactionsPast15Minutes() {
		List<Transaction> pendingTransactions = transactionRepository.findByStatusAndCreatedAtLessThanEqual(
				TransactionStatus.PENDING, DateTimeCreator.getTimeBeforeMinutes(15));
		if (pendingTransactions != null && !pendingTransactions.isEmpty()) {
			pendingTransactions.forEach(transaction -> {
				requestRouter.routeTransactionStatusEnquiry(transaction);
			});
		} else {
			System.out.println("No pending transactions found in past 5 minutes.");
		}
	}
	
	@Scheduled(cron = "0 */2 * * * *")
	public void fetchPendingTransactionsPast2Minutes() {
		List<Transaction> pendingTransactions = transactionRepository.findByStatusAndCreatedAtLessThanEqual(
				TransactionStatus.PENDING, DateTimeCreator.getTimeBeforeMinutes(2));
		if (pendingTransactions != null && !pendingTransactions.isEmpty()) {
			pendingTransactions.forEach(transaction -> {
				requestRouter.routeTransactionStatusEnquiry(transaction);
			});
		} else {
			System.out.println("No pending transactions found in past 2 minutes.");
		}
	}
}
