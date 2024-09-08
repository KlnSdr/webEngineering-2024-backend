package com.sks.recipes.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.sks")
public class RecipesServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RecipesServiceApplication.class, args);
	}

}
