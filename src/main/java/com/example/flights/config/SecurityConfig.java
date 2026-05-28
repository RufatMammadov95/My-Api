package com.example.flights.config;

import com.example.flights.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	private final JwtFilter jwtFilter;

	public SecurityConfig(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http.csrf(csrf -> csrf.disable())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

				.authorizeHttpRequests(auth -> auth

						.requestMatchers("/", "/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
								"/graphiql/**", "/graphql/**", "/oauth2/**", "/login/oauth2/**")
						.permitAll()

						.anyRequest().authenticated())

				.oauth2Login(oauth -> oauth.defaultSuccessUrl("/swagger-ui/index.html", true))

				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

				.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
