package com.example.flights.controller;

import com.example.flights.exception.GlobalExceptionHandler;
import com.example.flights.model.Flight;
import com.example.flights.repository.FlightRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

	@Mock
	private FlightRepository flightRepository;

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();

		mockMvc = MockMvcBuilders.standaloneSetup(new FlightController(flightRepository))
				.setControllerAdvice(new GlobalExceptionHandler())
				.setValidator(validator)
				.build();
	}

	@Test
	void getFlightsCapsPageSizeAtTwenty() throws Exception {
		when(flightRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

		new FlightController(flightRepository).getFlights(null, null, 0, 100);

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(flightRepository).findAll(pageableCaptor.capture());

		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(20);
	}

	@Test
	void createFlightRejectsInvalidRequestBody() throws Exception {
		mockMvc.perform(post("/api/flights")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.message").value("Validation failed"));
	}

	@Test
	void createFlightAcceptsDtoRequestBody() throws Exception {
		Flight savedFlight = new Flight(1L, "AZ100", "AZAL", "GYD", "IST", 120.0);
		when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);

		mockMvc.perform(post("/api/flights")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(savedFlight)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.flightNumber").value("AZ100"));
	}
}
