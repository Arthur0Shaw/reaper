package com.money.reaper.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.dto.InitiateTransactionRequest;
import com.money.reaper.dto.InitiateTransactionResponse;
import com.money.reaper.dto.StatusEnquiryRequest;
import com.money.reaper.model.Transaction;
import com.money.reaper.model.User;
import com.money.reaper.repository.TransactionRepository;
import com.money.reaper.repository.UserRepository;
import com.money.reaper.util.DateTimeCreator;
import com.money.reaper.util.KeyGeneratorUtil;
import com.money.reaper.util.TransactionStatus;
import com.money.reaper.util.TransactionType;
import com.money.reaper.util.ValidateStatusEnquiryRequest;
import com.money.reaper.util.ValidateTransacationRequest;

import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;

@Service
public class TransactionService {

	@Autowired
	private RequestRouter requestRouter;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ValidateTransacationRequest validateTransacationRequest;

	@Autowired
	private ValidateStatusEnquiryRequest validateStatusUpdateRequest;

	@Autowired
	private TransactionRepository transactionRepository;

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

			initiateTransactionResponse = modelMapper.map(transaction, InitiateTransactionResponse.class);

			if (!(boolean) validation[0]) {
				throw new RuntimeException((String) validation[1]);
			}
			transaction = requestRouter.routeNewTransaction(transaction);

			initiateTransactionResponse = modelMapper.map(transaction, InitiateTransactionResponse.class);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return initiateTransactionResponse;
	}

	public InitiateTransactionResponse transactionStatusUpdate(@Valid StatusEnquiryRequest request, String ipAddress) {

		InitiateTransactionResponse tansactionStatusResponse;
		try {
			User user = userRepository.findByUniqueId(request.getUniqueId());
			Object validation[] = validateStatusUpdateRequest.isValidStatusEnquiryRequest(user, request);

			if (!(boolean) validation[0]) {
				throw new RuntimeException((String) validation[1]);
			}

			Transaction transaction = null;
			if (request.getPgOrderId() != null && !request.getPgOrderId().trim().isEmpty()) {
				transaction = transactionRepository.findByPgOrderIdAndUniqueId(request.getPgOrderId(),
						request.getUniqueId());
			} else if (request.getMerchantOrderId() != null && !request.getMerchantOrderId().trim().isEmpty()) {
				transaction = transactionRepository.findByMerchantOrderIdAndUniqueId(request.getMerchantOrderId(),
						request.getUniqueId());
			} else {
				transaction = transactionRepository.findByMerchantOrderIdAndPgOrderIdAndUniqueId(
						request.getMerchantOrderId(), request.getPgOrderId(), request.getUniqueId());
			}

			if (transaction == null) {
				throw new RuntimeException("Transaction not found");
			}

			tansactionStatusResponse = modelMapper.map(transaction, InitiateTransactionResponse.class);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return tansactionStatusResponse;
	}

	public void handleAcquirerWebhook(String webhookPayload, String acquirer) {
		try {
			requestRouter.handleWebhook(webhookPayload, acquirer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
