package com.montesinos.apikey.manager.rest;

public class ApiKeyResponseError {

	private int status;
	private String message;
	private long timeStamp;
	
	public ApiKeyResponseError() {
		
	}

	public ApiKeyResponseError(int status, String message, long timeStamp) {
		super();
		this.status = status;
		this.message = message;
		this.timeStamp = timeStamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "ApiKeyResponseError [status=" + status + ", message=" + message + ", timeStamp=" + timeStamp + "]";
	}
	
	
	
}
