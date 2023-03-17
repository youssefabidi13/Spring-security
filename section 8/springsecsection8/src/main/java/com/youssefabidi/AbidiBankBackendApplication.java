package com.youssefabidi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug = true)
public class AbidiBankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbidiBankBackendApplication.class, args);
	}

}
