package com.poc.trainingmanager.exceptions.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poc.trainingmanager.exceptions.ResourceNotFoundException;
import com.poc.trainingmanager.model.StandardResponse;

@ControllerAdvice
public class RequestExceptionHandler {

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public StandardResponse<Object> handleInValidRequestException(RuntimeException exception) {
		StandardResponse<Object> errorResponse = new StandardResponse<Object>();
		errorResponse.setElement(exception.getCause());
		errorResponse.setMessage(exception.getMessage());
		return errorResponse;
	}

	/*
	 * @ExceptionHandler(value = { Exception.class })
	 * 
	 * @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	 * 
	 * @ResponseBody public StandardResponse<Object>
	 * handleGenericException(RuntimeException exception) { StandardResponse<Object>
	 * errorResponse = new StandardResponse<Object>();
	 * errorResponse.setElement(exception.getCause());
	 * errorResponse.setMessage(exception.getMessage()); return errorResponse; }
	 */
}
