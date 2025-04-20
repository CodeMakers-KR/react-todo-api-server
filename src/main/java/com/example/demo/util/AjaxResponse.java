package com.example.demo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class AjaxResponse {

	private int status;
	private String statusMessage;

	private int pages;
	private boolean next;
	
	private Map<String, Object> errors;

	private int count;
	private Object body;

	public static AjaxResponse OK() {
		return new AjaxResponse();
	}
	
	public static AjaxResponse OK(Object body) {
		return new AjaxResponse().setBody(body);
	}
	
	public static AjaxResponse OK(List<? extends Object> body) {
		return new AjaxResponse().setBody(body);
	}
	
	public static AjaxResponse OK(List<? extends Object> body, int count) {
		return new AjaxResponse().setBody(body, count);
	}
	
	public static AjaxResponse OK(Map<? extends Object, ? extends Object> body) {
		return new AjaxResponse().setBody(body);
	}
	
	public static AjaxResponse OK(Map<? extends Object, ? extends Object> body, int count) {
		return new AjaxResponse().setBody(body, count);
	}
	
	public static AjaxResponse CREATED() {
		return new AjaxResponse(HttpStatus.CREATED);
	}
	
	public static AjaxResponse NOT_FOUND() {
		return new AjaxResponse(HttpStatus.NOT_FOUND);
	}
	
	public static AjaxResponse BAD_REQUEST() {
		return new AjaxResponse(HttpStatus.BAD_REQUEST);
	}
	
	public AjaxResponse() {
		this(HttpStatus.OK);
	}
	
	public AjaxResponse(HttpStatus status) {
		this.status = status.value();
		this.statusMessage = status.getReasonPhrase();
	}

	public AjaxResponse setBody(Object body) {
		this.body = body;
		return this;
	}

	public AjaxResponse setBody(List<? extends Object> body) {
		this.body = body;
		this.count = body.size();
		return this;
	}

	public AjaxResponse setBody(List<? extends Object> body, int count) {
		this.body = body;
		this.count = count;
		return this;
	}

	public AjaxResponse setBody(Map<? extends Object, ? extends Object> body) {
		this.body = body;
		this.count = body.size();
		return this;
	}

	public AjaxResponse setBody(Map<? extends Object, ? extends Object> body, int count) {
		this.body = body;
		this.count = count;
		return this;
	}

	public AjaxResponse setErrorMessage(String message) {
		if (this.errors == null) {
			this.errors = new HashMap<>();
		}

		this.errors.putAll(Map.of("message", message));
		return this;
	}
	
	public AjaxResponse addError(Map<String, ?> error) {
		if (this.errors == null) {
			this.errors = new HashMap<>();
		}

		this.errors.putAll(error);
		return this;
	}

	public int getStatus() {
		return this.status;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public Map<String, Object> getErrors() {
		return this.errors;
	}

	public int getCount() {
		return this.count;
	}

	public Object getBody() {
		return this.body;
	}

	public int getPages() {
		return pages;
	}

	public AjaxResponse setPages(int pages) {
		this.pages = pages;
		return this;
	}

	public boolean getNext() {
		return next;
	}

	public AjaxResponse setNext(boolean next) {
		this.next = next;
		return this;
	}
	
	

}
