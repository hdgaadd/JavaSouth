package org.codeman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class RelaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelaxApplication.class, args);
	}

}
