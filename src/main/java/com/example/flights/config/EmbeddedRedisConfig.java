package com.example.flights.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Configuration
public class EmbeddedRedisConfig {

	private final int redisPort;
	private RedisServer redisServer;

	public EmbeddedRedisConfig(@Value("${spring.data.redis.port:6379}") int redisPort) {
		this.redisPort = redisPort;
	}

	@PostConstruct
	public void startRedis() throws IOException {
		if (isRedisRunning()) {
			return;
		}

		redisServer = new RedisServer(redisPort);
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() throws IOException {
		if (redisServer != null && redisServer.isActive()) {
			redisServer.stop();
		}
	}

	private boolean isRedisRunning() {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress("localhost", redisPort), 200);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
