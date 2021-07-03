package com.smallcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan({"com"})
public class SmallcaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmallcaseApplication.class, args);
	}

}
