package com.sks.recipes.service;

import com.sks.recipes.service.data.entity.AutoInsert;
import jakarta.persistence.PersistenceUnit;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.sks")
public class RecipesServiceApplication {

	public RecipesServiceApplication(AutoInsert autoInsert) {
		autoInsert.run();
	}

	public static void main(String[] args) {
		SpringApplication.run(RecipesServiceApplication.class, args);
	}

}
