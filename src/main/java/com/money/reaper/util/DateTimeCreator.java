package com.money.reaper.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeCreator {

	public static String getDateTime() {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return now.format(formatter);
	}

	public static String getDateIndex() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return today.format(formatter);
	}

	public static String getUpiGatewayDate(String createdAt) {
		return LocalDateTime.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
				.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	public static String getTimeBeforeMinutes(int i) {
		LocalDateTime nowMinus15Minutes = LocalDateTime.now().minusMinutes(15);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return nowMinus15Minutes.format(formatter);
	}

	public static String getTimeBeforeSeconds(int seconds) {
		LocalDateTime nowMinusSeconds = LocalDateTime.now().minusSeconds(seconds);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return nowMinusSeconds.format(formatter);
	}

}
