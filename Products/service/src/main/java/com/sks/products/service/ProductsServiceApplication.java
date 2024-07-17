package com.sks.products.service;

import com.sks.products.service.data.AutofillProductsOnFirstStart;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan("com.sks")
@Component
public class ProductsServiceApplication {

    public ProductsServiceApplication(AutofillProductsOnFirstStart autofillProductsOnFirstStart) {
        autofillProductsOnFirstStart.run();
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}

}
