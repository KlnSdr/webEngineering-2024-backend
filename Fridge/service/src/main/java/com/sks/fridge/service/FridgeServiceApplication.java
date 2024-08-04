package com.sks.fridge.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.sks")
public class FridgeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FridgeServiceApplication.class, args);
	}

}
