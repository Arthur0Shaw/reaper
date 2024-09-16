package com.money.reaper.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.model.Transaction;
import com.money.reaper.model.User;
import com.money.reaper.repository.TransactionRepository;

@Service
public class ValidateTransacationRequest {

	@Autowired
	private TransactionRepository transactionRepository;

	public Object[] isValidTransactionRequest(Transaction transaction, User user, String ipAddress,
			TransactionType transactionType) {
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
			
			if (Double.parseDouble(transaction.getAmount())>= 1.00) {
				responseObj[0] = false;
				responseObj[1] = "Transaction amount must be greater then or equal to 1.00";
				return responseObj;
			}

			if (!transactionRepository
					.findByUniqueIdAndMerchantOrderId(user.getUniqueId(), transaction.getMerchantOrderId())
					.isEmpty()) {
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
}
