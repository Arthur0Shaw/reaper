package com.money.reaper.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.dto.InitiateTransactionRequest;
import com.money.reaper.model.Transaction;
import com.money.reaper.model.User;
import com.money.reaper.repository.UserRepository;
import com.money.reaper.util.TransactionType;
import com.money.reaper.util.ValidateTransacationRequest;

import jakarta.validation.Valid;

@Service
public class TransactionService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ValidateTransacationRequest validateTransacationRequest;

	private final ModelMapper modelMapper = new ModelMapper(); // Direct instantiation

	public Transaction initiateNewTransaction(@Valid InitiateTransactionRequest request, String ipAddress) {
		try {
			Transaction transaction = modelMapper.map(request, Transaction.class);
			User user = userRepository.findByUniqueId(request.getUniqueId());
			Object validation[] = validateTransacationRequest.isValidTransactionRequest(transaction, user, ipAddress,
					TransactionType.SALE);
			if (!(boolean) validation[0]) {
				throw new RuntimeException((String) validation[1]);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
}
