package org.codeman;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@MapperScan({"org.codeman.mapper"})
public class Stater {

	public static void main(String[] args) {
		SpringApplication.run(Stater.class, args);
	}

}
