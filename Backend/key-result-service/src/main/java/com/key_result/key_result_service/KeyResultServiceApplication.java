package com.key_result.key_result_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class KeyResultServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyResultServiceApplication.class, args);
	}

}
