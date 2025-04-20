package com.example.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestGlobalExceptionHandler {

	@ExceptionHandler(PageNotFoundException.class)
	public Map<String, Object> viewErrorPage(PageNotFoundException runtimeException) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("message", runtimeException.getMessage());
		return resultMap;
	}
	
	@ExceptionHandler({FileNotExistsException.class, MakeXlsxFileException.class, RuntimeException.class})
	public Map<String, Object> viewErrorPage(RuntimeException runtimeException) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("message", runtimeException.getMessage());
		return resultMap;
	}
	
	@ExceptionHandler(AlreadyUseException.class)
	public Map<String, Object> viewMemberRegistErrorPage(AlreadyUseException exception) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("message", exception.getMessage());
		return resultMap;
	}
	
//	@ExceptionHandler(UserIdendifyNotMatchException.class)
//	public ModelAndView viewUserIdentifyNotMatchExceptionErrorPage(UserIdendifyNotMatchException exception) {
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("member/memberlogin");
//		modelAndView.addObject("memberVO", exception.getMemberVO());
//		modelAndView.addObject("message", exception.getMessage());
//		return modelAndView;
//	}
	
}