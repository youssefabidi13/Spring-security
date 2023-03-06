 package com.youssefabidi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.youssefabidi.controller")

public class AbidiBankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbidiBankBackendApplication.class, args);
	}

}
