package com.example.reg_keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class RegKeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegKeycloakApplication.class, args);
		System.out.println("Done");
	}

}
