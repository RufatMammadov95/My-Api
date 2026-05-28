package com.example.flights.dto;

import java.util.Map;

public class ErrorResponse {

	private boolean success;
	private String message;
	private Map<String, String> errors;

	public ErrorResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public ErrorResponse(boolean success, String message, Map<String, String> errors) {
		this.success = success;
		this.message = message;
		this.errors = errors;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
}