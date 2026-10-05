package com.dmantz.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LmsBApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsBApplication.class, args);
	}

}
