package com.money.reaper.util;

import java.time.LocalDate;
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
}
