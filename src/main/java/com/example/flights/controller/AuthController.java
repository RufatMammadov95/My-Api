package com.example.flights.controller;

import com.example.flights.model.User;
import com.example.flights.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepository;

	public AuthController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("/signup")
	public String registerUser(@RequestBody User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			return "Error: This username already exists!";
		}
		userRepository.save(user);
		return "User successfully registered!";
	}
}