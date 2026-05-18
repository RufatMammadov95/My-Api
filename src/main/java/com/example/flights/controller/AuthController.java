package com.example.flights.controller;

import com.example.flights.model.User;
import com.example.flights.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final org.springframework.security.crypto.password.PasswordEncoder encoder;

	public AuthController(UserRepository userRepository,
			org.springframework.security.crypto.password.PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.encoder = encoder;
	}

	@PostMapping("/signup")
	public String registerUser(@RequestBody User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			return "Error: This name is already taken!";
		}
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		return "Registration completed!";
	}
}