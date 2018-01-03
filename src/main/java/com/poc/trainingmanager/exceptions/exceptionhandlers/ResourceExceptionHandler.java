package com.poc.trainingmanager.exceptions.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poc.trainingmanager.exceptions.AccessDeniedException;
import com.poc.trainingmanager.exceptions.ResourceNotFoundException;
import com.poc.trainingmanager.model.StandardResponse;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public StandardResponse<Object> handleResourceNotFoundException(RuntimeException exception) {

		StandardResponse<Object> errorResponse = new StandardResponse<Object>();
		errorResponse.setElement(exception);
		errorResponse.setMessage(exception.getMessage());
		return errorResponse;
	}

	@ExceptionHandler(value = { AccessDeniedException.class })
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public StandardResponse<Object> handleAccessDeniedException(RuntimeException exception) {

		StandardResponse<Object> errorResponse = new StandardResponse<Object>();
		errorResponse.setElement(exception);
		errorResponse.setMessage(exception.getMessage());
		return errorResponse;
	}
}
