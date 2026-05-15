package com.example.flights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, RedisAutoConfiguration.class })
public class GlobalFlightsApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(GlobalFlightsApiApplication.class, args);
	}
}