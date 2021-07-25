package org.daas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class DaasApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaasApplication.class, args);
	}

}
