package com.example.flights.controller;

import com.example.flights.model.Flight;
import com.example.flights.repository.FlightRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class FlightGraphQLController {

	private final FlightRepository flightRepository;

	public FlightGraphQLController(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	@QueryMapping
	public List<Flight> allFlights() {
		return flightRepository.findAll();
	}

	@QueryMapping
	public Flight flightById(@Argument Long id) {
		return flightRepository.findById(id).orElse(null);
	}
}