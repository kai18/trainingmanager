package com.poc.trainingmanager.exceptions;

/**
 * This is a checked runtime exception that is to be thrown when a user sends 
 * a request with argument mismatch type.
 * The client should be returned an HTTP 400.
 *
 */

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -5034790529027817164L;
	public BadRequestException(String message) {
		super(message);
	}
} 