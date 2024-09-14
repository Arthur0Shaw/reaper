package com.money.reaper.util;

import java.security.SecureRandom;
import java.util.UUID;

public class KeyGeneratorUtil {

	public static String generateUniqueId() {
		SecureRandom random = new SecureRandom();
		StringBuilder uniqueId = new StringBuilder(16);
		for (int i = 0; i < 16; i++) {
			uniqueId.append(random.nextInt(10)); // Append 0-9 digits
		}
		return uniqueId.toString();
	}

	public static String generateApiKey() {
		return UUID.randomUUID().toString().replace("-", ""); // Generates a 32-character key
	}
}
