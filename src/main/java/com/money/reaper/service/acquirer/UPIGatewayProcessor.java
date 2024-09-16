package com.money.reaper.service.acquirer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.money.reaper.model.Transaction;
import com.money.reaper.util.TransactionStatus;

import io.micrometer.common.util.StringUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class UPIGatewayProcessor {

	@Value("${UPIGateway.key}")
	private String upiGatewayKey;

	@Value("${UPIGateway.redirectURL}")
	private String upiGatewayRedirectURL;

	@Value("${UPIGateway.initURL}")
	private String upiGatewayInitURL;

	private static final OkHttpClient client = new OkHttpClient();
	private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

	public Transaction initiatePayment(Transaction transaction) {
		JSONObject acquirerRequest = prepareInitRequest(transaction);
		RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, acquirerRequest.toString());
		Request request = new Request.Builder().url(upiGatewayInitURL).post(body)
				.addHeader("Content-Type", "application/json").build();
		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				JSONObject jsonResponse = new JSONObject(responseBody);
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
			} else {
				System.out.println("Error: Response not successful or body is null");
				setTransactionError(transaction);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setTransactionError(transaction);
		}
		return transaction;
	}

	private JSONObject prepareInitRequest(Transaction transaction) {
		JSONObject acquirerRequest = new JSONObject();
		acquirerRequest.put("key", upiGatewayKey);
		acquirerRequest.put("client_txn_id", transaction.getPgOrderId());
		acquirerRequest.put("amount", transaction.getAmount());
		acquirerRequest.put("p_info", "UPI Payment");
		acquirerRequest.put("customer_name", transaction.getName());
		acquirerRequest.put("customer_email", transaction.getEmail());
		acquirerRequest.put("customer_mobile", transaction.getMobile());
		acquirerRequest.put("redirect_url", upiGatewayRedirectURL);
		return acquirerRequest;
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
		transaction.setStatus(TransactionStatus.ERROR);
		transaction.setPgResponseCode(TransactionStatus.ERROR.getCode());
		transaction.setPgResponseMessage(TransactionStatus.ERROR.getDisplayName());
	}
}
