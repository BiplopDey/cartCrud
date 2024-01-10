package com.assignment.cartCrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CartCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartCrudApplication.class, args);
	}

}
