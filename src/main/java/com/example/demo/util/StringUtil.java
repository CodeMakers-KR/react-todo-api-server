package com.example.demo.util;

public abstract class StringUtil {
	private StringUtil() {}
	
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	
	public static int parseInt(Object obj) {
		if (obj == null) {
			return 0;
		}
		
		String str = obj.toString();
		return parseInt(str);
	}
	
	public static int parseInt(String str) {
		if (isEmpty(str)) {
			return 0;
		}
		
		if (str.matches("^[0-9]+$")) {
			return Integer.parseInt(str);
		}
		
		return 0;
	}
}
