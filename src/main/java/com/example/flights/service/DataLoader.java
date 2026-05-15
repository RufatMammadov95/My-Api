package com.example.flights.service;

import com.example.flights.model.Flight;
import com.example.flights.repository.FlightRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class DataLoader implements CommandLineRunner {

	private final FlightRepository flightRepository;

	public DataLoader(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		if (flightRepository.count() > 0)
			return;

		System.out.println(">> CSV reading begins...");

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new ClassPathResource("flights.csv").getInputStream(), StandardCharsets.UTF_8));

			CSVParser csvParser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true)
					.setIgnoreHeaderCase(true).setTrim(true).setAllowMissingColumnNames(true).build().parse(reader);

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

					flightRepository.save(flight);
					count++;
				} catch (Exception e) {
					continue;
				}
			}
			System.out.println(">> ROW " + count + " ADDED TO BASE!");
			csvParser.close();

		} catch (Exception e) {
			System.err.println(">> I COULDN'T READ CSV! Error:" + e.getMessage());
		}
	}
}