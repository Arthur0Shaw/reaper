package com.money.reaper.util;

import java.security.SecureRandom;
import java.util.UUID;

public class KeyGeneratorUtil {

	private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final SecureRandom RANDOM = new SecureRandom();

	public static String generateUniqueId() {
		StringBuilder uniqueId = new StringBuilder(16);
		for (int i = 0; i < 16; i++) {
			uniqueId.append(RANDOM.nextInt(10)); // Append 0-9 digits
		}
		return uniqueId.toString();
	}

	public static String generateApiKey() {
		return UUID.randomUUID().toString().replace("-", ""); // Generates a 32-character key
	}

	public static String generateRandomId() {
		int length = 12 + RANDOM.nextInt(5);
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = RANDOM.nextInt(ALPHANUMERIC_CHARACTERS.length());
			sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
		}
		return sb.toString();
	}
}
