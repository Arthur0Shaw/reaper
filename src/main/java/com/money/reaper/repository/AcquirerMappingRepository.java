package com.money.reaper.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.money.reaper.model.AcquirerMapping;

@Service
@Repository
public interface AcquirerMappingRepository extends MongoRepository<AcquirerMapping, String> {

	List<AcquirerMapping> findAll();

	@Query(value = "{ 'bank': ?0 }", fields = "{ 'vpa': 1, '_id': 0 }")
    String findVpaByBank(String bank);
}
