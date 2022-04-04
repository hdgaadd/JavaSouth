package com.codeman;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@MapperScan({"com.codeman.mapper", "com.codeman.dao"})
public class RelaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelaxApplication.class, args);
	}

}
