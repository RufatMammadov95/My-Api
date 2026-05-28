package com.example.flights.controller;

import com.example.flights.model.Flight;
import com.example.flights.repository.FlightRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Controller
public class FlightGraphQLController {

	private final FlightRepository flightRepository;

	public FlightGraphQLController(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	@QueryMapping
	public List<Flight> allFlights(@Argument Integer page, @Argument Integer size) {

		if (page == null)
			page = 0;
		if (size == null)
			size = 20;

		page = Math.max(page, 0);
		size = Math.max(1, Math.min(size, 20));

		return flightRepository.findAll(PageRequest.of(page, size)).getContent();
	}

	@QueryMapping
	public Flight flightById(@Argument Long id) {
		return flightRepository.findById(id).orElse(null);
	}
}