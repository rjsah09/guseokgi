package com.yang.guseokgi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GuseokgiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuseokgiApplication.class, args);
	}

}
