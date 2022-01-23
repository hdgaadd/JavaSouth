package com.codeman;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class RelaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelaxApplication.class, args);
	}

}
