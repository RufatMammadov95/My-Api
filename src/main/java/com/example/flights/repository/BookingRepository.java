package com.example.flights.repository;

import com.example.flights.model.Booking;
import com.example.flights.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByUser(User user);
}