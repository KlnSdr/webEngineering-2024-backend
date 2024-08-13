package com.sks.fridge.service;

import com.sks.fridge.service.data.entity.AutoInsert;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.sks")
public class FridgeServiceApplication {

	public FridgeServiceApplication(AutoInsert autoInsert) {
		autoInsert.run();
	}

	public static void main(String[] args) {
		SpringApplication.run(FridgeServiceApplication.class, args);
	}

}
