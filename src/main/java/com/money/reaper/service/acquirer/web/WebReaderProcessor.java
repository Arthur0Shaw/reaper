package com.money.reaper.service.acquirer.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.reaper.model.AcquirerRoutingDetails;
import com.money.reaper.model.Transaction;
import com.money.reaper.model.User;
import com.money.reaper.repository.AcquirerMappingRepository;
import com.money.reaper.repository.AcquirerRoutingDetailsRespository;
import com.money.reaper.repository.TransactionRepository;
import com.money.reaper.repository.UserRepository;
import com.money.reaper.util.DateTimeCreator;
import com.money.reaper.util.TransactionStatus;

import io.micrometer.common.util.StringUtils;

@Service
public class WebReaderProcessor {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AcquirerMappingRepository acquirerMappingRepository;

	@Autowired
	private AcquirerRoutingDetailsRespository acquirerRoutingDetailsRespository;

	Map<String, User> userIdMap = new HashMap<String, User>();

	private static final Logger logger = LoggerFactory.getLogger(WebReaderProcessor.class);

	public Transaction initiatePayment(Transaction transaction) {
		try {
			String acquirer = getAcquirerForProcessing(transaction);
			if (StringUtils.isBlank(acquirer)) {
				logger.error("No eligible acquirer found for routing the transaction with ID: " + transaction.getId()
						+ " Merchant Order ID: " + transaction.getMerchantOrderId());
				transaction.setStatus(TransactionStatus.FAILURE);
				transaction.setPgResponseCode(TransactionStatus.FAILURE.getCode());
				transaction.setPgResponseMessage(TransactionStatus.FAILURE.getDisplayName());
				return transaction;
			}
			transaction.setAcquirer(acquirer);
			String vpa = getRandomVpa(acquirer);
			String businessName;
			if (userIdMap.containsKey(transaction.getUniqueId())) {
				User user = userIdMap.get(transaction.getUniqueId());
				businessName = user.getBusinessName();
			} else {
				User user = userRepository.findByUniqueId(transaction.getUniqueId());
				userIdMap.put(transaction.getUniqueId(), user);
				businessName = user.getBusinessName();
			}

			String intents = generateUpiIntent(vpa, businessName, transaction.getAmount(), transaction.getPgOrderId(),
					transaction.getPgOrderId());
			transaction.setIntentURL(intents);
		} catch (Exception e) {
			logger.error("Exception occurred during transaction initiation: ", e);
			transaction.setStatus(TransactionStatus.FAILURE);
			transaction.setPgResponseCode(TransactionStatus.FAILURE.getCode());
			transaction.setPgResponseMessage(TransactionStatus.FAILURE.getDisplayName());
		}
		return transaction;
	}

	private String getAcquirerForProcessing(Transaction transaction) {
		List<Transaction> transactions = transactionRepository.findByUniqueIdAndDateIndex(transaction.getUniqueId(),
				DateTimeCreator.getDateIndex());
		String totalTodayAmount = String
				.valueOf(transactions.stream().mapToDouble(t -> Double.parseDouble(t.getAmount())).sum());
		String transactionAmount = transaction.getAmount();
		List<AcquirerRoutingDetails> eligibleAcquirerRoutingDetailsList = acquirerRoutingDetailsRespository
				.findByThresholdTxnAmountAndMaximumTxnAmountGreaterThan(totalTodayAmount, transactionAmount);
		if (!eligibleAcquirerRoutingDetailsList.isEmpty()) {
			Map<String, Long> acquirerTransactionCounts = transactionRepository
					.countTransactionsByAcquirer(DateTimeCreator.getDateIndex());
			for (AcquirerRoutingDetails acquirerDetails : eligibleAcquirerRoutingDetailsList) {
				String acquirerBank = acquirerDetails.getBank();
				int acquirerTotalTxnCount = Integer.parseInt(acquirerDetails.getTotalTxnCount());
				Long transactionCount = acquirerTransactionCounts.get(acquirerBank);
				if (transactionCount != null && acquirerTotalTxnCount < transactionCount) {
					logger.info(acquirerBank + " is selected for processing this transaction with ID: "
							+ transaction.getId());
					return acquirerBank;
				}
			}
			logger.error("No eligible acquirer found with transaction count lower than limits.");
			transaction.setStatus(TransactionStatus.FAILURE);
			transaction.setPgResponseCode(TransactionStatus.FAILURE.getCode());
			transaction.setPgResponseMessage(TransactionStatus.FAILURE.getDisplayName());
			return null;
		} else {
			logger.error("Transaction limit exceeded, no acquirer found eligible to route this transaction, ID: "
					+ transaction.getId());
			transaction.setStatus(TransactionStatus.FAILURE);
			transaction.setPgResponseCode(TransactionStatus.FAILURE.getCode());
			transaction.setPgResponseMessage(TransactionStatus.FAILURE.getDisplayName());
			return null;
		}
	}

	public String getRandomVpa(String acquirer) {
		String vpaString = acquirerMappingRepository.findVpaByBank(acquirer);
		if (vpaString == null || vpaString.isEmpty()) {
			throw new RuntimeException("No VPA found for the bank: " + acquirer);
		}
		String[] vpaArray = vpaString.split("\\s*,\\s*");
		if (vpaArray.length == 0) {
			throw new RuntimeException("No valid VPA found for the bank: " + acquirer);
		}
		int randomIndex = ThreadLocalRandom.current().nextInt(vpaArray.length);
		return vpaArray[randomIndex];
	}

	public static String generateUpiIntent(String vpa, String pn, String amount, String tr, String tn) {
		String baseUpiUrl = String.format("pay?pa=%s&pn=%s&am=%s&tr=%s&tn=%s", vpa, pn, amount, tr, tn);
		JSONObject upiApps = new JSONObject();
		upiApps.put("PHONE_PE", "phonepe://upi/" + baseUpiUrl);
		upiApps.put("GOOGLE_PAY", "tez://upi/" + baseUpiUrl);
		upiApps.put("PAYTM", "paytmmp://pay?" + baseUpiUrl);
		upiApps.put("BHIM", "bhim://upi/" + baseUpiUrl);
		upiApps.put("CRED", "cred://upi/" + baseUpiUrl);
		upiApps.put("AMAZON_PAY", "amazonpay://upi/" + baseUpiUrl);
		String plainUpiIntent = "upi://" + baseUpiUrl;
		upiApps.put("UPI_INTENT", plainUpiIntent);
		return upiApps.toString();
	}
}
