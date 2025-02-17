package com.config_server.config_server_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerServiceApplication.class, args);
	}

}
