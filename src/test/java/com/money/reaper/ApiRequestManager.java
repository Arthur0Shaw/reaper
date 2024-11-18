package com.money.reaper;

import okhttp3.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiRequestManager {
	private static String csrfToken;
	private static String laravelSession;
	private static String jsessionId;
	private static String tsCookie;
	private static String rnumber; // Extracted runtime number from secondAPICall

	public static void main(String[] args) throws IOException {
		// Step 1: Perform zeroAPICall to fetch tokens
		zeroAPICall();
		System.out.println("#######");

		// Step 2: Perform firstAPICall using tokens
		firstAPICall();
		System.out.println("#######");

		// Step 3: Perform secondAPICall using extracted cookies
		secondAPICall();
		System.out.println("#######");

		// Step 4: Perform thirdAPICall using extracted cookies and hashed password
		thirdAPICall();
	}

	public static void zeroAPICall() throws IOException {
		String url = "https://www.cityunionbank.com/cub-net-banking-cub-online-banking";

		OkHttpClient client = new OkHttpClient.Builder().build();

		Request request = new Request.Builder().url(url).get().addHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
				.addHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.70 Safari/537.36")
				.build();

		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful() && response.headers("Set-Cookie") != null) {
				for (String cookie : response.headers("Set-Cookie")) {
					if (cookie.startsWith("XSRF-TOKEN=")) {
						csrfToken = extractToken(cookie, "XSRF-TOKEN");
					} else if (cookie.startsWith("laravel_session=")) {
						laravelSession = extractToken(cookie, "laravel_session");
					}
				}
			} else {
				throw new IOException("Failed to fetch tokens from zeroAPICall. Response Code: " + response.code());
			}
		}
	}

	public static void firstAPICall() throws IOException {
		String url = "https://www.onlinecub.net/servlet/ibs.servlets.IBSLoginServlet";

		OkHttpClient client = new OkHttpClient.Builder().build();

		RequestBody body = RequestBody.create("HandleID=START&ApplnFlag=A",
				MediaType.parse("application/x-www-form-urlencoded"));

		Request request = new Request.Builder().url(url).post(body)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.addHeader("Origin", "https://www.cityunionbank.com")
				.addHeader("Referer", "https://www.cityunionbank.com/cub-net-banking-cub-online-banking")
				.addHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.70 Safari/537.36")
				.addHeader("Cookie", "XSRF-TOKEN=" + csrfToken + "; laravel_session=" + laravelSession).build();

		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful() && response.headers("Set-Cookie") != null) {
				for (String cookie : response.headers("Set-Cookie")) {
					if (cookie.startsWith("JSESSIONID=")) {
						jsessionId = extractToken(cookie, "JSESSIONID");
					} else if (cookie.startsWith("TS0189bde8=")) {
						tsCookie = extractToken(cookie, "TS0189bde8");
					}
				}
			} else {
				throw new IOException("Failed to perform firstAPICall. Response Code: " + response.code());
			}
		}
	}

	public static void secondAPICall() throws IOException {
		String url = "https://www.onlinecub.net/servlet/ibs.servlets.IBSLoginServlet";

		OkHttpClient client = new OkHttpClient.Builder().build();

		RequestBody body = RequestBody.create(
				"HandleID=I_MFA_DETAILS&browserName=Chrome&browserVersion=130&osName=Windows&osVersion=Windows+10&request_type=2&ApplnFlag=A&uid1=null&uid=6449374",
				MediaType.parse("application/x-www-form-urlencoded"));

		Request request = new Request.Builder().url(url).post(body)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.addHeader("Origin", "https://www.onlinecub.net").addHeader("Referer", "https://www.onlinecub.net/")
				.addHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.70 Safari/537.36")
				.addHeader("Cookie", "JSESSIONID=" + jsessionId + "; TS0189bde8=" + tsCookie).build();

		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful()) {
				String responseBody = response.body().string();
				System.out.println("Second API Response Code: " + response.code());
				System.out.println("Response Body:\n" + responseBody);

				// Extract rnumber from the response body
				rnumber = extractRNumber(responseBody);
				if (rnumber == null) {
					throw new IOException("Failed to extract rnumber from the response.");
				}
				System.out.println("Extracted rnumber: " + rnumber);
			} else {
				throw new IOException("Failed to perform secondAPICall. Response Code: " + response.code());
			}
		}
	}

	public static void thirdAPICall() throws IOException {
		String url = "https://www.onlinecub.net/servlet/ibs.servlets.IBSLoginServlet";

		String uid = "6449374";
		String password = "Sebastian@93"; // Replace with actual password
		String hashedPassword = hashPassword(uid, password);

		OkHttpClient client = new OkHttpClient.Builder().build();

		RequestBody body = RequestBody.create(
				"HandleID=LogIn&browserName=Chrome&browserVersion=130&osName=Windows&osVersion=Windows+10&request_type=2&ApplnFlag=A&uid1="
						+ uid + "&uid=" + uid + "&ref1=ABC%40%401432&pwd=" + hashedPassword
						+ "&ref2=ABC%40%401432&MFACheckBox=on",
				MediaType.parse("application/x-www-form-urlencoded"));

		Request request = new Request.Builder().url(url).post(body)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.addHeader("Origin", "https://www.onlinecub.net").addHeader("Referer", "https://www.onlinecub.net/")
				.addHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.70 Safari/537.36")
				.addHeader("Cookie", "JSESSIONID=" + jsessionId + "; TS0189bde8=" + tsCookie).build();

		try (Response response = client.newCall(request).execute()) {
			System.out.println("Third API Response Code: " + response.code());
			System.out.println("Response Body:\n" + response.body().string());
			if (!response.isSuccessful()) {
				throw new IOException("Failed to perform thirdAPICall. Response Code: " + response.code());
			}
		}
	}

	private static String extractToken(String cookie, String key) {
		try {
			return cookie.split(key + "=")[1].split(";")[0];
		} catch (Exception e) {
			System.out.println("Failed to parse cookie for " + key + ": " + cookie);
			return null;
		}
	}

	private static String extractRNumber(String htmlResponse) {
		String regex = "var\\s+rnumber\\s*=\\s*(\\d+);";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(htmlResponse);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static String hashPassword(String uid, String password) {
		try {
			String temp = sha1(uid + password + "IBSKey");
			return sha1(temp + rnumber);
		} catch (Exception e) {
			throw new RuntimeException("Error hashing password", e);
		}
	}

	private static String sha1(String input) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] bytes = md.digest(input.getBytes());
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
