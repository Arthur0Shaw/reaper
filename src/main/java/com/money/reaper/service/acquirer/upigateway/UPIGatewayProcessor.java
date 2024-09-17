package com.money.reaper.service.acquirer.upigateway;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.money.reaper.model.Transaction;
import com.money.reaper.repository.TransactionRepository;
import com.money.reaper.util.DateTimeCreator;
import com.money.reaper.util.TransactionStatus;

import io.micrometer.common.util.StringUtils;

@Service
public class UPIGatewayProcessor {

	@Autowired
	private Communicator communicator;

	@Value("${UPIGateway.key}")
	private String upiGatewayKey;

	@Value("${UPIGateway.redirectURL}")
	private String upiGatewayRedirectURL;

	@Value("${UPIGateway.initURL}")
	private String upiGatewayInitURL;

	@Value("${UPIGateway.statusURL}")
	private String upiGatewayStatusURL;

	@Autowired
	private TransactionRepository transactionRepository;

	public Transaction initiatePayment(Transaction transaction) {
		try {
			String acquirerRequest = prepareInitRequest(transaction);
			JSONObject jsonResponse = communicator.initiateJSONAPICall(acquirerRequest, upiGatewayInitURL);
			if (jsonResponse.getBoolean("status") && jsonResponse.has("data")) {
				JSONObject data = jsonResponse.getJSONObject("data");
				String acquirerReferenceId = String.valueOf(data.getInt("order_id"));
				String paymentURL = data.getString("payment_url");
				if (StringUtils.isNotBlank(paymentURL)) {
					String intentUrl = extactIntentUrlFromURL(paymentURL);
					transaction.setIntentURL(intentUrl);
					transaction.setAcquirerReferenceId(acquirerReferenceId);
				} else {
					System.out.println("Error: PaymentURL is missing");
					setTransactionError(transaction);
				}
			} else {
				System.out.println("Error: Status is not true or data is missing");
				setTransactionError(transaction);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setTransactionError(transaction);
		}
		return transaction;
	}

	public Transaction initiatePaymentStatus(Transaction transaction) {
		try {
			String acquirerRequest = preparePaymentStatusRequest(transaction);
			JSONObject jsonResponse = communicator.initiateJSONAPICall(acquirerRequest, upiGatewayStatusURL);

			if (jsonResponse.getBoolean("status")) {
				JSONObject data = jsonResponse.getJSONObject("data");

				String customerVpa = data.optString("customer_vpa", "NA");
				String upiTxnId = data.optString("upi_txn_id", "NA");
				String status = data.optString("status", "NA");
				String remark = data.optString("remark", "NA");
				String upiId = data.getJSONObject("Merchant").optString("upi_id", "NA");

				transaction.setCustomerUpiId(customerVpa);
				transaction.setRrn(upiTxnId);
				transaction.setAcquirerResponseMessage(remark);
				transaction.setMerchantUpiId(upiId);

				if (status.equalsIgnoreCase("Success")) {
					transaction.setStatus(TransactionStatus.SUCCESS);
					transaction.setPgResponseCode(TransactionStatus.SUCCESS.getCode());
					transaction.setPgResponseMessage(TransactionStatus.SUCCESS.getDisplayName());
				} else if (status.equalsIgnoreCase("Failure") || status.equalsIgnoreCase("Close")) {
					transaction.setStatus(TransactionStatus.FAILURE);
					transaction.setPgResponseCode(TransactionStatus.FAILURE.getCode());
					transaction.setPgResponseMessage(TransactionStatus.FAILURE.getDisplayName());
				} else {
					transaction.setStatus(TransactionStatus.PENDING);
					transaction.setPgResponseCode(TransactionStatus.PENDING.getCode());
					transaction.setPgResponseMessage(TransactionStatus.PENDING.getDisplayName());
				}
			} else {
				System.out.println("Status is false, no extraction performed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setTransactionError(transaction);
		}
		return transaction;
	}

	private String preparePaymentStatusRequest(Transaction transaction) {
		JSONObject acquirerRequest = new JSONObject();
		acquirerRequest.put("key", upiGatewayKey);
		acquirerRequest.put("client_txn_id", transaction.getPgOrderId());
		acquirerRequest.put("txn_date", DateTimeCreator.getUpiGatewayDate(transaction.getCreatedAt()));
		return acquirerRequest.toString();
	}

	private String prepareInitRequest(Transaction transaction) {
		JSONObject acquirerRequest = new JSONObject();
		acquirerRequest.put("key", upiGatewayKey);
		acquirerRequest.put("client_txn_id", transaction.getPgOrderId());
		acquirerRequest.put("amount", transaction.getAmount());
		acquirerRequest.put("p_info", "UPI Payment");
		acquirerRequest.put("customer_name", transaction.getName());
		acquirerRequest.put("customer_email", transaction.getEmail());
		acquirerRequest.put("customer_mobile", transaction.getMobile());
		acquirerRequest.put("redirect_url", upiGatewayRedirectURL);
		return acquirerRequest.toString();
	}

	private String extactIntentUrlFromURL(String paymentURL) {
		try {
			Document doc = fetchHTML(paymentURL);
			String base64Image = extractBase64FromHTML(doc);
			if (base64Image != null) {
				byte[] imageBytes = Base64.getDecoder().decode(base64Image);
				ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
				BufferedImage bufferedImage = ImageIO.read(bis);
				BinaryBitmap binaryBitmap = new BinaryBitmap(
						new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
				Result result = new MultiFormatReader().decode(binaryBitmap);
				return result.getText();
			}
		} catch (NotFoundException e) {
			System.out.println("QR Code not found in the image.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Document fetchHTML(String urlString) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		return Jsoup.parse(connection.getInputStream(), "UTF-8", urlString);
	}

	private static String extractBase64FromHTML(Document doc) {
		Element imgTag = doc.selectFirst("img.qr_code_img");
		if (imgTag != null) {
			String imgSrc = imgTag.attr("src");
			if (imgSrc != null && imgSrc.startsWith("data:image/png;base64,")) {
				return imgSrc.substring("data:image/png;base64,".length());
			}
		}
		return null;
	}

	private void setTransactionError(Transaction transaction) {
		transaction.setStatus(TransactionStatus.FAILURE);
		transaction.setPgResponseCode(TransactionStatus.FAILURE.getCode());
		transaction.setPgResponseMessage(TransactionStatus.FAILURE.getDisplayName());
	}

	public Transaction handleWebhook(String webhookPayload) {
		Map<String, String> keyValueMap = extractQueryParams(webhookPayload);
		Transaction transaction = transactionRepository.findByPgOrderId(getSafeValue(keyValueMap, "client_txn_id"));
		String status = getSafeValue(keyValueMap, "status");
		transaction.setCustomerUpiId(getSafeValue(keyValueMap, "customer_vpa"));
		transaction.setRrn(getSafeValue(keyValueMap, "upi_txn_id"));
		transaction.setAcquirerResponseMessage(getSafeValue(keyValueMap, "remark"));
		transaction.setMerchantUpiId(getSafeValue(keyValueMap, "upi_id"));

		if (status.equalsIgnoreCase("Success")) {
			transaction.setStatus(TransactionStatus.SUCCESS);
			transaction.setPgResponseCode(TransactionStatus.SUCCESS.getCode());
			transaction.setPgResponseMessage(TransactionStatus.SUCCESS.getDisplayName());
		} else if (status.equalsIgnoreCase("Failure") || status.equalsIgnoreCase("Close")) {
			transaction.setStatus(TransactionStatus.FAILURE);
			transaction.setPgResponseCode(TransactionStatus.FAILURE.getCode());
			transaction.setPgResponseMessage(TransactionStatus.FAILURE.getDisplayName());
		} else {
			transaction.setStatus(TransactionStatus.PENDING);
			transaction.setPgResponseCode(TransactionStatus.PENDING.getCode());
			transaction.setPgResponseMessage(TransactionStatus.PENDING.getDisplayName());
		}
		return transaction;
	}

	private Map<String, String> extractQueryParams(String queryString) {
		Map<String, String> keyValueMap = new HashMap<>();
		String[] pairs = queryString.split("&");
		for (String pair : pairs) {
			String[] keyValue = pair.split("=", 2);
			String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
			String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";
			keyValueMap.put(key, value);
		}
		return keyValueMap;
	}

	private String getSafeValue(Map<String, String> keyValueMap, String key) {
		String value = keyValueMap.getOrDefault(key, "");
		return value.isEmpty() ? "NA" : value;
	}
}
