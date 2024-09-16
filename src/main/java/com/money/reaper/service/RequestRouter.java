package com.money.reaper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.model.Transaction;
import com.money.reaper.repository.TransactionRepository;
import com.money.reaper.service.acquirer.AcquirerManagerFactory;

@Service
public class RequestRouter {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AcquirerManagerFactory acquirerManagerFactory;
	
	public void route(Transaction transaction) {
		// TODO Auto-generated method stub
		
		transactionRepository.save(transaction);
	}

}
