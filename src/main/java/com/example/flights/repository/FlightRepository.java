package com.example.flights.repository;

import com.example.flights.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
	Page<Flight> findByDepartureCityAndArrivalCity(String departureCity, String arrivalCity, Pageable pageable);
}