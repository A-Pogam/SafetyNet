package com.SafetyNet.SafetyNet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.SafetyNet.SafetyNet.model") // Location of folder model
@EnableJpaRepositories(basePackages = "com.SafetyNet.SafetyNet.repository") // location repository


public class SafetyNetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetApplication.class, args);
	}

}
