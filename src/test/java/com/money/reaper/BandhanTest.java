package com.money.reaper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BandhanTest {

	private static String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTREFMQUw1MDgiLCJsbiI6IkRBTEFMIiwicm9sZSI6InJldGFpbHVzZXIiLCJpaWQiOiJlTFU2eFU1MGlFUTZja0Z4Ulp3dTRDNXBqVjdlU2UiLCJkb2IiOiIyMDk3MDczMTAwMDAwMC4wWiIsImZuIjoiU0FOREVFUCIsImlhc3QiOiIxODAwMDAiLCJleHAiOjE3MjY4NDIxNDgsImlhdCI6MTcyNjY2MjE0OCwiYXAiOiJBUElOVEVSTkVUIn0.ak1xFZX1lky_CKNrbiJ_LchQob3Ffbqk-1KW9_FLacw";
	private static String COOKIE = "secretKey=TFoqUSSKn87EeNDQ5iXqpw7mJVqu9klx; TS01568bcc=01a27636673d5672c8a764d54512120ed9a04113c4aad6d1609fbfb3709da61aa345c18fa97cff77eeb6c4a2bd010ce144fec0aa8ed78fe45a7a5fca08c6ef9d7dd044f6ce43dd3288958944ae038ebc75da6ffa26; TS015d33ac=01f9b8bb76ff399acdc42d566b68ef8be6d3aa91133cc397d9d7eaa9d86a639a3abcf1c7c3604f90991fca2b439aebc031796767c2; srv_id=ccd9330fe64a1b7ed38b770a84fd6735; TS012e4d32=01a2763667cb76deb92546ac88921c6a44fbe4391b723ec8e7e99afa8b15c5d20a05eaf4a278ea5810f8787028eac3885972d1ac36b719e12c1239c8794c7ed724f0138606";
	private static String STATEMENT_URL = "https://retail.bandhanbank.com/bandhan/digx-common/dda/v1/demandDeposit/C7E721E1C9E41D436CEB514D818D6424A8767F966F616DFD718EDF38365EC7A48B7A5645/transactions?fromDate=2024-09-18&searchBy=SPD&toDate=2024-09-18&transactionType=A&locale=en";
	private static String UPDATE_EMAIL = "https://retail.bandhanbank.com/bandhan/digx-common/user/v1/me/party?locale=en";

	public static void main(String[] args) {
		boolean toggle = true;
		while (true) {
			try {
				String currentUrl = toggle ? UPDATE_EMAIL : STATEMENT_URL;
				String responseString = checkStatement(currentUrl);

				if (currentUrl.equalsIgnoreCase(STATEMENT_URL)) {
					JSONObject jsonObject = new JSONObject(responseString);
					JSONArray itemsArray = jsonObject.getJSONArray("items");
					for (int i = 0; i < itemsArray.length(); i++) {
						JSONObject item = itemsArray.getJSONObject(i);
						double amount = item.getJSONObject("amountInAccountCurrency").getDouble("amount");
						String description = item.getString("description");
						JSONObject key = item.getJSONObject("key");
						String transactionDate = item.getString("transactionDate");

						System.out.println("Amount: " + amount);
						System.out.println("Description: " + description);
						System.out.println("Key: " + key);
						System.out.println("Transaction Date: " + transactionDate);
						System.out.println("#####################");
					}
				}
				// System.out.println("Response from " + (toggle ? "STATEMENT_URL" :
				// "UPDATE_EMAIL") + ": " + responseString);
				toggle = !toggle;
				TimeUnit.SECONDS.sleep(30);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				//System.out.println(responseString);
			}
		}
	}

	public static String checkStatement(String url) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(url).get()
				.addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
				.addHeader("accept-encoding", "gzip, deflate, br, zstd")
				.addHeader("accept-language", "en-IN,en;q=0.9,hi;q=0.8").addHeader("authorization", TOKEN)
				.addHeader("Connection", "keep-alive").addHeader("Content-Type", "application/json")
				.addHeader("Cookie", COOKIE).addHeader("Dnt", "1").addHeader("Host", "retail.bandhanbank.com")
				.addHeader("Sec-Ch-Ua", "\"Chromium\";v=\"128\", \"Not;A=Brand\";v=\"24\", \"Google Chrome\";v=\"128\"")
				.addHeader("Sec-Ch-Ua-Mobile", "?0").addHeader("Sec-Ch-Ua-Platform", "\"macOS\"")
				.addHeader("sec-fetch-dest", "empty").addHeader("Sec-Fetch-Mode", "cors")
				.addHeader("Sec-Fetch-Site", "same-origin")
				.addHeader("User-Agent",
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36")
				.addHeader("X-Requested-With", "XMLHttpRequest").addHeader("x-target-unit", "OBDX_BU")
				.addHeader("X-Token-Type", "JWT").build();

		Response response = client.newCall(request).execute();

		if ("gzip".equalsIgnoreCase(response.header("Content-Encoding"))) {
			GZIPInputStream gzipInputStream = new GZIPInputStream(response.body().byteStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(gzipInputStream, "UTF-8"));
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
			return stringBuilder.toString();
		} else {
			return response.body().string();
		}
	}
}
