package com.example.flights.controller;

import com.example.flights.model.Flight;
import com.example.flights.repository.FlightRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
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

		page = Math.max(page, 0);
		size = Math.max(1, Math.min(size, 20));

		if (origin != null && dest != null) {
			return flightRepository.findByDepartureCityAndArrivalCity(origin, dest, PageRequest.of(page, size));
		}

		return flightRepository.findAll(PageRequest.of(page, size));
	}

	@GetMapping("/{id}")
	public Flight getFlightById(@PathVariable Long id) {
		return flightRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@CacheEvict(value = "flightsCache", allEntries = true)
	public Flight createFlight(@RequestBody Flight flight) {
		flight.setId(null);
		return flightRepository.save(flight);
	}

	@PutMapping("/{id}")
	@CacheEvict(value = "flightsCache", allEntries = true)
	public Flight updateFlight(@PathVariable Long id, @RequestBody Flight flightRequest) {
		Flight flight = flightRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));

		flight.setFlightNumber(flightRequest.getFlightNumber());
		flight.setAirline(flightRequest.getAirline());
		flight.setDepartureCity(flightRequest.getDepartureCity());
		flight.setArrivalCity(flightRequest.getArrivalCity());
		flight.setPrice(flightRequest.getPrice());

		return flightRepository.save(flight);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@CacheEvict(value = "flightsCache", allEntries = true)
	public void deleteFlight(@PathVariable Long id) {
		if (!flightRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found");
		}

		flightRepository.deleteById(id);
	}
}
