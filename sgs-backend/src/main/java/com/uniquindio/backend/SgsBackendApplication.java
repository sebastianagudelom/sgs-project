package com.uniquindio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SgsBackendApplication {

	/* Main method to run the Spring Boot application */
	public static void main(String[] args) {
		SpringApplication.run(SgsBackendApplication.class, args);
		System.out.println("SGS Backend Application is running...");
	}






}
