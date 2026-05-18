package com.example.flights.controller;

import com.example.flights.model.Booking;
import com.example.flights.model.Flight;
import com.example.flights.model.User;
import com.example.flights.repository.BookingRepository;
import com.example.flights.repository.FlightRepository;
import com.example.flights.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingRepository bookingRepository;
	private final FlightRepository flightRepository;
	private final UserRepository userRepository;

	public BookingController(BookingRepository bookingRepository, FlightRepository flightRepository,
			UserRepository userRepository) {
		this.bookingRepository = bookingRepository;
		this.flightRepository = flightRepository;
		this.userRepository = userRepository;
	}

	@PostMapping("/{flightId}")
	public String bookFlight(@PathVariable Long flightId, Principal principal) {
		User user = userRepository.findByUsername(principal.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new RuntimeException("Flight not found"));

		Booking booking = new Booking();
		booking.setUser(user);
		booking.setFlight(flight);
		bookingRepository.save(booking);

		return "Flight booked successfully! Reservation ID: " + booking.getId();
	}

	@GetMapping
	public List<Booking> getMyBookings(Principal principal) {
		User user = userRepository.findByUsername(principal.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		return bookingRepository.findByUser(user);
	}

	@PutMapping("/{bookingId}/change-flight/{newFlightId}")
	public String updateBooking(@PathVariable Long bookingId, @PathVariable Long newFlightId, Principal principal) {

		User user = userRepository.findByUsername(principal.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		if (!booking.getUser().getId().equals(user.getId())) {
			return "Error: You can only update your own bookings!";
		}

		Flight newFlight = flightRepository.findById(newFlightId)
				.orElseThrow(() -> new RuntimeException("New flight not found"));

		booking.setFlight(newFlight);
		bookingRepository.save(booking);

		return "Booking updated successfully! New Flight ID: " + newFlight.getId();
	}

	@DeleteMapping("/{bookingId}")
	public String deleteBooking(@PathVariable Long bookingId, Principal principal) {

		User user = userRepository.findByUsername(principal.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		if (!booking.getUser().getId().equals(user.getId())) {
			return "Error: You can only delete your own bookings!";
		}

		bookingRepository.delete(booking);

		return "Booking (ID: " + bookingId + ") deleted successfully!";
	}
}