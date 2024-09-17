package com.money.reaper.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import com.money.reaper.dto.StatusEnquiryRequest;
import com.money.reaper.model.User;

@Service
public class ValidateStatusEnquiryRequest {

	public Object[] isValidStatusEnquiryRequest(User user, StatusEnquiryRequest request) {
		Object[] responseObj = new Object[2];
		try {

			// Check if user is a merchant
			if (!user.getUserType().equals(UserType.MERCHANT)) {
				responseObj[0] = false;
				responseObj[1] = "User not allowed to enquire about the transaction";
				return responseObj;
			}

			// Check that either pgOrderId or merchantOrderId is provided
			if ((request.getPgOrderId() == null || request.getPgOrderId().trim().isEmpty())
					&& (request.getMerchantOrderId() == null || request.getMerchantOrderId().trim().isEmpty())) {
				responseObj[0] = false;
				responseObj[1] = "Either pgOrderId or merchantOrderId must be provided.";
				return responseObj;
			}

			// Validate the hash
			if (!isValidHash(user, request)) {
				responseObj[0] = false;
				responseObj[1] = "Invalid HASH";
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

	private boolean isValidHash(User user, StatusEnquiryRequest request) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(request.getMerchantOrderId() != null ? request.getMerchantOrderId() : "").append("|");
			sb.append(request.getPgOrderId() != null ? request.getPgOrderId() : "").append("|");

			String computedHash = computeSHA256(sb.append(user.getApiKey()).toString());

			return computedHash.equals(request.getHash());
		} catch (NoSuchAlgorithmException e) {
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
