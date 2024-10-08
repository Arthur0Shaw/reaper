package com.money.reaper.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.money.reaper.model.User;
import com.money.reaper.util.UserStatus;
import com.money.reaper.util.UserType;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByEmail(String email);

	Optional<User> findByMobile(String mobile);

	boolean existsByEmail(String email);

	boolean existsByMobile(String mobile);

	List<User> findByUserTypeAndUserStatus(UserType userType, UserStatus userStatus);

	User findByUniqueId(String uniqueId);
}
