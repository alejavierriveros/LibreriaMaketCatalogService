package com.example.libreriaMarketCatalogService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LibreriaMarketCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibreriaMarketCatalogServiceApplication.class, args);
	}

}
