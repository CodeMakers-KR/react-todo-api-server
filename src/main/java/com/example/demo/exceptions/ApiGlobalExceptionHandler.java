package com.example.demo.exceptions;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.util.AjaxResponse;

@RestControllerAdvice
public class ApiGlobalExceptionHandler {
	
	@ExceptionHandler(PageNotFoundException.class)
	public Object viewErrorPage(PageNotFoundException runtimeException) {
		return AjaxResponse.NOT_FOUND().setErrorMessage(runtimeException.getMessage());
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Object viewErrorPage(HttpMessageNotReadableException runtimeException) {
		return AjaxResponse.BAD_REQUEST().setErrorMessage(runtimeException.getMessage().split(":") [0]);
	}

	@ExceptionHandler({ FileNotExistsException.class, MakeXlsxFileException.class, RuntimeException.class })
	public Object viewErrorPage(RuntimeException runtimeException) {
		runtimeException.printStackTrace();
		return AjaxResponse.NOT_FOUND().setErrorMessage(runtimeException.getMessage());
	}

	@ExceptionHandler(AlreadyUseException.class)
	public Object viewMemberRegistErrorPage(AlreadyUseException exception) {
		return AjaxResponse.NOT_FOUND().setErrorMessage(exception.getMessage());
	}

}
