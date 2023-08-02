package com.project.mapdagu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
public class MapdaguApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapdaguApplication.class, args);
	}

}
