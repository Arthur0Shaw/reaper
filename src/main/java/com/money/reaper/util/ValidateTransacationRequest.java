package com.money.reaper.util;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.dto.InitiateTransactionRequest;
import com.money.reaper.model.Transaction;
import com.money.reaper.model.User;
import com.money.reaper.repository.TransactionRepository;

import jakarta.validation.Valid;

@Service
public class ValidateTransacationRequest {

	@Autowired
	private TransactionRepository transactionRepository;

	public Object[] isValidTransactionRequest(Transaction transaction, User user, String ipAddress,
			TransactionType transactionType, @Valid InitiateTransactionRequest request) {
		Object[] responseObj = new Object[2];
		try {
			if (!user.getUserStatus().equals(UserStatus.ACTIVE)) {
				responseObj[0] = false;
				responseObj[1] = "User not active";
				return responseObj;
			}

			if (!user.getUserType().equals(UserType.MERCHANT)) {
				responseObj[0] = false;
				responseObj[1] = "User not allowed to initiate the Transaction";
				return responseObj;
			}

			if (!(user.getWhitelistedIps().contains(ipAddress) || ipAddress.equalsIgnoreCase("0.0.0.0"))) {
				responseObj[0] = false;
				responseObj[1] = "IP address not allowed to initiate the Transaction";
				return responseObj;
			}

			if (Double.parseDouble(transaction.getAmount()) < 1.00) {
				responseObj[0] = false;
				responseObj[1] = "Transaction amount must be greater then or equal to 1.00";
				return responseObj;
			}

			if (!isValidHash(transaction, user, request)) {
				responseObj[0] = false;
				responseObj[1] = "Invalid HASH";
				return responseObj;
			}

			if (!transactionRepository
					.findByUniqueIdAndMerchantOrderId(user.getUniqueId(), transaction.getMerchantOrderId()).isEmpty()) {
				responseObj[0] = false;
				responseObj[1] = "Duplicate merchant_order_id, use unique to initiate the Transaction";
				return responseObj;
			}

		} catch (Exception e) {
			responseObj[0] = false;
			responseObj[1] = "Something went wrong";
			return responseObj;
		}
		responseObj[0] = true;
		responseObj[1] = "Success";
		return responseObj;
	}

	private boolean isValidHash(Transaction transaction, User user, @Valid InitiateTransactionRequest request) {
		try {
			String[] fieldNames = { "amount", "client_txn_id", "customer_email", "customer_mobile", "customer_name",
					"product_info", "redirect_url", "udf1", "udf2", "udf3", "uniqueId" };
			StringBuilder sb = new StringBuilder();
			for (String fieldName : fieldNames) {
				Field field = InitiateTransactionRequest.class.getDeclaredField(fieldName);
				field.setAccessible(true);
				Object value = field.get(transaction);
				if (value == null) {
					sb.append("|");
				} else {
					sb.append(value.toString()).append("|");
				}
			}
			if (sb.length() > 0) {
				sb.setLength(sb.length() - 1);
			}
			String computedHash = computeSHA256(sb.append("|").append(user.getApiKey()).toString());
			return computedHash.equals(request.getHash());
		} catch (NoSuchAlgorithmException | NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static String computeSHA256(String data) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashBytes = digest.digest(data.getBytes());
		return Hex.encodeHexString(hashBytes);
	}
}
