package com.example.flights.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FlightRequest {

	@NotBlank(message = "Flight number is required")
	private String flightNumber;

	@NotBlank(message = "Airline is required")
	private String airline;

	@NotBlank(message = "Departure city is required")
	private String departureCity;

	@NotBlank(message = "Arrival city is required")
	private String arrivalCity;

	@NotNull(message = "Price is required")
	@Min(value = 0, message = "Price cannot be negative")
	private Double price;

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}