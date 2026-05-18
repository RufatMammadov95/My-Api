package com.example.flights.controller;

import com.example.flights.model.Flight;
import com.example.flights.repository.FlightRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

	private final FlightRepository flightRepository;

	public FlightController(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	@GetMapping
	@Cacheable(value = "flightsCache", key = "#origin + '-' + #dest + '-' + #page + '-' + #size")
	public Page<Flight> getFlights(@RequestParam(required = false) String origin,
			@RequestParam(required = false) String dest, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {

		if (origin != null && dest != null) {
			return flightRepository.findByDepartureCityAndArrivalCity(origin, dest, PageRequest.of(page, size));
		}

		return flightRepository.findAll(PageRequest.of(page, size));
	}
}