package com.example.flights.controller;

import com.example.flights.dto.ApiResponse;
import com.example.flights.model.User;
import com.example.flights.repository.UserRepository;
import com.example.flights.security.JwtUtil;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final org.springframework.security.crypto.password.PasswordEncoder encoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	public AuthController(UserRepository userRepository,
			org.springframework.security.crypto.password.PasswordEncoder encoder,
			AuthenticationManager authenticationManager, JwtUtil jwtUtil) {

		this.userRepository = userRepository;
		this.encoder = encoder;
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {

		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiResponse(false, "This name is already taken!"));
		}

		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);

		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Registration completed!"));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestBody User loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		String token = jwtUtil.generateToken(authentication.getName());

		return ResponseEntity.ok(new ApiResponse(true, token));
	}
}