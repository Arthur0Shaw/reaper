package com.money.reaper.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.dto.InitiateTransactionRequest;
import com.money.reaper.dto.InitiateTransactionResponse;
import com.money.reaper.model.Transaction;
import com.money.reaper.model.User;
import com.money.reaper.repository.UserRepository;
import com.money.reaper.util.DateTimeCreator;
import com.money.reaper.util.KeyGeneratorUtil;
import com.money.reaper.util.TransactionStatus;
import com.money.reaper.util.TransactionType;
import com.money.reaper.util.ValidateTransacationRequest;

import jakarta.validation.Valid;

@Service
public class TransactionService {

	@Autowired
	private RequestRouter requestRouter;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ValidateTransacationRequest validateTransacationRequest;

	private final ModelMapper modelMapper = new ModelMapper(); // Direct instantiation

	public InitiateTransactionResponse initiateNewTransaction(@Valid InitiateTransactionRequest request,
			String ipAddress) {
		

		InitiateTransactionResponse initiateTransactionResponse;
		try {
			Transaction transaction = modelMapper.map(request, Transaction.class);
			User user = userRepository.findByUniqueId(request.getUniqueId());
			Object validation[] = validateTransacationRequest.isValidTransactionRequest(transaction, user, ipAddress,
					TransactionType.SALE, request);
			transaction.setStatus(TransactionStatus.PENDING);
			transaction.setPgResponseCode(TransactionStatus.PENDING.getCode());
			transaction.setPgResponseMessage(TransactionStatus.PENDING.getDisplayName());
			transaction.setPgOrderId(KeyGeneratorUtil.generateRandomId());
			transaction.setCreatedAt(DateTimeCreator.getDateTime());
			transaction.setUpdatedAt(DateTimeCreator.getDateTime());
			transaction.setDateIndex(DateTimeCreator.getDateIndex());
			
			initiateTransactionResponse = modelMapper.map(transaction,
					InitiateTransactionResponse.class);
			
			if (!(boolean) validation[0]) {
				throw new RuntimeException((String) validation[1]);
			}
			transaction = requestRouter.route(transaction);
			
			initiateTransactionResponse = modelMapper.map(transaction,
					InitiateTransactionResponse.class);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return initiateTransactionResponse;
	}
}
