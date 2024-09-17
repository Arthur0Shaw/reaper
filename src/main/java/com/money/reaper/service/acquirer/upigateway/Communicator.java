package com.money.reaper.service.acquirer.upigateway;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class Communicator {

	private static final OkHttpClient client = new OkHttpClient();
	private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

	public JSONObject initiateJSONAPICall(String acquirerRequest, String url) throws IOException {
		RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, acquirerRequest.toString());
		Request request = new Request.Builder().url(url).post(body).addHeader("Content-Type", "application/json")
				.build();
		Response response = client.newCall(request).execute();
		if (response.isSuccessful() && response.body() != null) {
			String responseBody = response.body().string();
			return new JSONObject(responseBody);
		} else {
			System.out.println("Error: Response not successful or body is null");
			throw new IOException("Error: Response not successful or body is null");
		}
	}

}
