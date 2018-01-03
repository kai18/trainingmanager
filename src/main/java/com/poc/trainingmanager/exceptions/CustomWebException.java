package com.poc.trainingmanager.exceptions;

public class CustomWebException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8524207221042761475L;

	public CustomWebException(String message) {
		super(message);
	}
}
