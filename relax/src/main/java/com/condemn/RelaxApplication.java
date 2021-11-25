package com.condemn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
//@ComponentScan(basePackages = {"com.condemn"})
@MapperScan("com.condemn.mapper")
public class RelaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelaxApplication.class, args);
	}

}
