package com.poc.trainingmanager.exceptions;

/**
 * This is a checked runtime exception that is to be thrown when a user with
 * specified information is not found in the database or some other scenario.
 * The client should be returned an HTTP 404.
 *
 */
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5034790529027817164L;

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
