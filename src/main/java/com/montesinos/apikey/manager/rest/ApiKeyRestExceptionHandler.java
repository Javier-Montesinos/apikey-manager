package com.montesinos.apikey.manager.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiKeyRestExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ApiKeyResponseError> handleException(ApiKeyNotFoundException e) {
		ApiKeyResponseError error = new ApiKeyResponseError();
		
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(e.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		return new ResponseEntity<ApiKeyResponseError>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiKeyResponseError> handleException(Exception e) {
		ApiKeyResponseError error = new ApiKeyResponseError();
		
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(e.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		return new ResponseEntity<ApiKeyResponseError>(error, HttpStatus.BAD_REQUEST);
	}

}
