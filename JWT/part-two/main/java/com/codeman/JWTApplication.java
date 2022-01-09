package com.codeman;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.codeman.mapper")
public class JWTApplication {

	public static void main(String[] args) {
		SpringApplication.run(JWTApplication.class, args);
	}

}
