package com.example.flights.controller;

import com.example.flights.model.User;
import com.example.flights.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;

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

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody User loginRequest) {
		Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid username or password!");
		}

		User user = userOpt.get();

		if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
			return ResponseEntity.ok("Login successful!");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid username or password!");
		}
	}
}