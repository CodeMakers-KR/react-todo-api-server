package com.example.demo.exceptions;

public class FileNotExistsException extends RuntimeException {

	private static final long serialVersionUID = -144069918626360058L;

	public FileNotExistsException(String message) {
		super(message);
	}
	
}
