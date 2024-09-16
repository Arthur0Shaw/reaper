package com.money.reaper.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.money.reaper.model.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String>{
	

    List<Transaction> findAllByOrderByCreatedAtAsc();

    List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByDateIndexBetween(LocalDate startDate, LocalDate endDate);

    List<Transaction> findByStatusAndIdAndAcquirerReferenceIdAndMerchantOrderId(
        String status, String id, String acquirerReferenceId, String merchantOrderId);
}
