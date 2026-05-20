package com.example.flights.controller;

import com.example.flights.model.User;
import com.example.flights.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final org.springframework.security.crypto.password.PasswordEncoder encoder;
	private final AuthenticationManager authenticationManager;

	public AuthController(UserRepository userRepository,
			org.springframework.security.crypto.password.PasswordEncoder encoder, AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.authenticationManager = authenticationManager;
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
	public ResponseEntity<String> loginUser(@RequestBody User loginRequest, HttpServletRequest request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authentication);
			SecurityContextHolder.setContext(context);
			request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					context);

			return ResponseEntity.ok("Login successful!");
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid username or password!");
		}
	}
}
