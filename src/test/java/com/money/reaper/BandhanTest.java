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

	private static String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTREFMQUw1MDgiLCJsbiI6IkRBTEFMIiwicm9sZSI6InJldGFpbHVzZXIiLCJpaWQiOiJpVmljeHVNbmczMjFCQlJPOWM0R2N6U3l3VXB4TmwiLCJkb2IiOiIyMDk3MDczMTAwMDAwMC4wWiIsImZuIjoiU0FOREVFUCIsImlhc3QiOiIxODAwMDAiLCJleHAiOjE3MzExMzU4MjcsImlhdCI6MTczMDk1NTgyNywiYXAiOiJBUElOVEVSTkVUIn0.v2uoAeH1aprqfbfcWvgMsPxAj3OPN_KXY_U5UGIDCn4";
	private static String COOKIE = "secretKey=OaZ4o73p4VUoA6srbB0qLUoTkGfXkgqj; TS01568bcc=01a2763667dbe5a4fd1049bb13507504b56e5f44a6392ede575d69e56871e2b50b6455696a0b152b88bc93788405aab09cfbbc60df48eb1db8a2924c1a40d2cb9e8ca2fbcb3ae455303bd13a84a939a817df5210c5; srv_id=fca44270817269024077f7d5163c0ddd; TS012e4d32=01a2763667ae1441a71d4d5f615c51562dce583f2f392ede575d69e56871e2b50b6455696a0b152b88bc93788405aab09cfbbc60dfa71518572f79e38938a8d4b25718df61";
	private static String STATEMENT_URL = "https://retail.bandhanbank.com/bandhan/digx-common/dda/v1/demandDeposit/272B0209F2A691388172D53AFDB88A4EBA69127115FD8311F1CD591D04898A61DCE8A002/transactions?fromDate=2024-11-07&searchBy=SPD&toDate=2024-11-07&transactionType=A&locale=en";
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
