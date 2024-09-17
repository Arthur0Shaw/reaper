package com.money.reaper.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.money.reaper.dto.DashboardResponseData;
import com.money.reaper.model.Transaction;
import com.money.reaper.util.TransactionStatus;

@Service
@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

	List<Transaction> findAllByOrderByCreatedAtDesc();

	List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	@Aggregation(pipeline = { "{ $match: { 'dateIndex': { $gte: ?0, $lte: ?1 } } }", "{ $sort: { 'createdAt': -1 } }" })
	List<Transaction> findByDateIndexBetween(String dateIndexFrom, String dateIndexTo);

	List<Transaction> findByStatusAndIdAndAcquirerReferenceIdAndMerchantOrderId(String status, String id,
			String acquirerReferenceId, String merchantOrderId);

	List<Transaction> findByUniqueIdAndMerchantOrderId(String uniqueId, String merchantOrderId);

	@Aggregation(pipeline = { "{ $match: { 'dateIndex': { $gte: ?0, $lte: ?1 } } }",
			"{ $group: { _id: '$dateIndex', totalCount: { $sum: 1 }, totalAmount: { $sum: { $toDouble: '$amount' } } } }",
			"{ $group: { _id: null, totalCount: { $sum: '$totalCount' }, totalAmount: { $sum: '$totalAmount' }, "
					+ "dateWiseTransactionCount: { $push: { k: '$_id', v: '$totalCount' } }, "
					+ "dateWiseTransactionAmount: { $push: { k: '$_id', v: '$totalAmount' } } } }",
			"{ $project: { _id: 0, totalCount: 1, totalAmount: 1, "
					+ "dateWiseTransactionCount: { $arrayToObject: '$dateWiseTransactionCount' }, "
					+ "dateWiseTransactionAmount: { $arrayToObject: '$dateWiseTransactionAmount' } } }" })
	DashboardResponseData findAllAggregatedTransactions(String dateIndexFrom, String dateIndexTo);

	@Aggregation(pipeline = { "{ $match: { 'status': ?0, 'dateIndex': { $gte: ?1, $lte: ?2 } } }",
			"{ $group: { _id: '$dateIndex', totalCount: { $sum: 1 }, totalAmount: { $sum: { $toDouble: '$amount' } } } }",
			"{ $group: { _id: null, totalCount: { $sum: '$totalCount' }, totalAmount: { $sum: '$totalAmount' }, "
					+ "dateWiseTransactionCount: { $push: { k: '$_id', v: '$totalCount' } }, "
					+ "dateWiseTransactionAmount: { $push: { k: '$_id', v: '$totalAmount' } } } }",
			"{ $project: { _id: 0, totalCount: 1, totalAmount: 1, "
					+ "dateWiseTransactionCount: { $arrayToObject: '$dateWiseTransactionCount' }, "
					+ "dateWiseTransactionAmount: { $arrayToObject: '$dateWiseTransactionAmount' } } }" })
	DashboardResponseData findAggregatedTransactionsByStatus(TransactionStatus status, String dateIndexFrom,
			String dateIndexTo);

	Transaction findByMerchantOrderIdAndUniqueId(String merchantOrderId, String uniqueId);

	Transaction findByPgOrderIdAndUniqueId(String pgOrderId, String uniqueId);

	Transaction findByMerchantOrderIdAndPgOrderIdAndUniqueId(String merchantOrderId, String pgOrderId, String uniqueId);

	@Query("{ 'status': ?0, 'createdAt': { $lte: ?1 } }")
	List<Transaction> findByStatusAndCreatedAtLessThanEqual(TransactionStatus status, String createdAtThreshold);
}
