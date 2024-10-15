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

	private static String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTREFMQUw1MDgiLCJsbiI6IkRBTEFMIiwicm9sZSI6InJldGFpbHVzZXIiLCJpaWQiOiI5NDRWOUdNend3R0xBYXNVWkxVUEZHbW1BcGtEcEciLCJkb2IiOiIyMDk3MDczMTAwMDAwMC4wWiIsImZuIjoiU0FOREVFUCIsImlhc3QiOiIxODAwMDAiLCJleHAiOjE3MjcxMDEzODUsImlhdCI6MTcyNjkyMTM4NSwiYXAiOiJBUElOVEVSTkVUIn0.JCHZ-knq2XBPuqTg9lscGPWny9UBBUubDllqRiQZka0";
	private static String COOKIE = "secretKey=VexS2eICj4960RxNZebqYISngCwmd9vt; TS01568bcc=01a2763667f560170aa34ffbf55305e19241ffc706ec0bc50de2ccc0b00a313c0785b9d26ab7def79bf3cc4d01521db97d89393ddf1292fcfd04da81b71ad31528d54bacaba8c4b217d4b6cf003c1808408a281114; srv_id=2df39bb7d94cc09617ca8f7d67b583a8; TS012e4d32=01a2763667bae671432d06278c54fa11b636fb4c61df64b4bd20e8a3aab50347a1b526403bd18c4805d364f4c53553f940dcf6c1c7879cd3e93f65c52ca570aba0617b64a2";
	private static String STATEMENT_URL = "https://retail.bandhanbank.com/bandhan/digx-common/dda/v1/demandDeposit/100AC9DA4F5DA30B9EF14E2BC00425440A9F3F7D5724C2FCE3CD7C00B1F9AB2D5DAE0752/transactions?fromDate=2024-09-21&searchBy=SPD&toDate=2024-09-21&transactionType=A&locale=en";
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
