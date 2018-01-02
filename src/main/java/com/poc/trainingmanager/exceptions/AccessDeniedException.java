package com.poc.trainingmanager.exceptions;

public class AccessDeniedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -114950769111510799L;

	public AccessDeniedException(String message) {
		super(message);
	}
}
