package com.example.demo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateUtil {

	private DateUtil() {}
	
	public static String today() {
		LocalDateTime ldt = LocalDateTime.now();
		return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
	
	public static String date(String datetime) {
		LocalDateTime ldt = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
