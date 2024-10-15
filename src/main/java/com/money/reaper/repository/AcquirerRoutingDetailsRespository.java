package com.money.reaper.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.money.reaper.model.AcquirerRoutingDetails;

@Service
@Repository
public interface AcquirerRoutingDetailsRespository extends MongoRepository<AcquirerRoutingDetails, String> {

	List<AcquirerRoutingDetails> findAll();
	
	@Query(value = "{ 'thresholdTxnAmount': { $gt: ?0 }, 'maximumTxnAmount': { $gt: ?1 } }")
    List<AcquirerRoutingDetails> findByThresholdTxnAmountAndMaximumTxnAmountGreaterThan(
    		String totalTodayAmount, String transactionAmount);

}
