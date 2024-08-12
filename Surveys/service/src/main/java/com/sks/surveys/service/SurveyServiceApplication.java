package com.sks.surveys.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.sks")
public class SurveyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveyServiceApplication.class, args);
	}

}
