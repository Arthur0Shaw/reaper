package com.money.reaper.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.money.reaper.model.UserLoginHistory;

@Repository
public interface UserLoginHistoryRepository extends MongoRepository<UserLoginHistory, String> {

	List<UserLoginHistory> findAll();
}
