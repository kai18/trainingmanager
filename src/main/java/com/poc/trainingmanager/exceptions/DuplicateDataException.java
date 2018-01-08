package com.poc.trainingmanager.exceptions;

/**
 * This is a checked runtime exception that is to be thrown when a user with
 * specific data is already existing in the database. It is used when duplicate values are not allowed.
 * The client should be returned an HTTP 403.
 *
 */

public class DuplicateDataException extends RuntimeException {

	private static final long serialVersionUID = -5034790529027817164L;
	public DuplicateDataException(String message) {
		super(message);
	}
} 
