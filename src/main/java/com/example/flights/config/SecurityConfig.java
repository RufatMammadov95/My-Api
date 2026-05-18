package com.example.flights.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**", "/login/**", "/oauth2/**")
						.permitAll().requestMatchers(org.springframework.http.HttpMethod.GET, "/api/flights/**")
						.permitAll().requestMatchers(org.springframework.http.HttpMethod.GET, "/graphql/**").permitAll()
						.anyRequest().authenticated())
				.httpBasic(withDefaults()).oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/api/flights", true));

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}