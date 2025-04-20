package com.example.demo.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public abstract class RequestUtil {

	private RequestUtil() {
		
	}
	
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	public static HttpServletResponse getResponse() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
	}
	
	public static HttpSession getSession() {
		return getRequest().getSession();
	}
	
	public static boolean isStartsWithURI(String uri) {
		return getRequest().getRequestURI().startsWith(uri);
	}
	
	public static boolean isApiRequest() {
		return isStartsWithURI("/api/");
	}
}
