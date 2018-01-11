package com.poc.trainingmanager.exceptions;

public class InvalidRequestDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5740290051414655467L;

	public InvalidRequestDataException(String message) {
		super(message);
	}
}
