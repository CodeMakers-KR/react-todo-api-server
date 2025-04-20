package com.example.demo.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PageNotFoundException.class)
	public ModelAndView viewErrorPage(PageNotFoundException runtimeException) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/404");
		modelAndView.addObject("message", runtimeException.getMessage());
		return modelAndView;
	}
	
	@ExceptionHandler({FileNotExistsException.class, MakeXlsxFileException.class, RuntimeException.class})
	public ModelAndView viewErrorPage(RuntimeException runtimeException) {
		
		runtimeException.printStackTrace();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/500");
		modelAndView.addObject("message", runtimeException.getMessage());
		return modelAndView;
	}
	
	@ExceptionHandler(AlreadyUseException.class)
	public ModelAndView viewMemberRegistErrorPage(AlreadyUseException exception) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("member/memberregist");
		modelAndView.addObject("memberVO", exception.getMemberVO());
		return modelAndView;
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