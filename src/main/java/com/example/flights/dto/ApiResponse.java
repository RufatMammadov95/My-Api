package com.example.flights.dto;

public class ApiResponse {
	private boolean success;
	private String message;
	private String token;

	public ApiResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public ApiResponse(boolean success, String message, String token) {
		this.success = success;
		this.message = message;
		this.token = token;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public String getToken() {
		return token;
	}
}
