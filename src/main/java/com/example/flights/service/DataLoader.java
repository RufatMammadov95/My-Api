package com.example.flights.service;

import com.example.flights.model.Flight;
import com.example.flights.repository.FlightRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test")
@ConditionalOnProperty(name = "app.data-loader.enabled", havingValue = "true", matchIfMissing = true)
public class DataLoader implements CommandLineRunner {

	private final FlightRepository flightRepository;

	public DataLoader(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		if (flightRepository.count() > 0)
			return;

		System.out.println(">> CSV reading begins...");

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new ClassPathResource("flights.csv").getInputStream(), StandardCharsets.UTF_8))) {
			CSVParser csvParser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true)
					.setIgnoreHeaderCase(true).setTrim(true).setAllowMissingColumnNames(true).build().parse(reader);

			List<Flight> flights = new ArrayList<>();
			int count = 0;
			for (CSVRecord record : csvParser) {
				if (count >= 1100)
					break;

				try {
					Flight flight = new Flight();

					flight.setAirline(record.get("UniqueCarrier"));
					flight.setFlightNumber(record.get("FlightNum"));
					flight.setDepartureCity(record.get("Origin"));
					flight.setArrivalCity(record.get("Dest"));
					flight.setPrice(50.0 + (Math.random() * 450.0));

					flights.add(flight);
					count++;
				} catch (Exception e) {
					continue;
				}
			}
			flightRepository.saveAll(flights);
			System.out.println(">> ROW " + count + " ADDED TO BASE!");
			csvParser.close();

		} catch (Exception e) {
			System.err.println(">> I COULDN'T READ CSV! Error:" + e.getMessage());
		}
	}
}
