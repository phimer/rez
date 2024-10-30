package com.aero;

import com.aero.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@SpringBootApplication
public class RezzApplication {

	public static void main(String[] args) {
		SpringApplication.run(RezzApplication.class, args);
	}

}
