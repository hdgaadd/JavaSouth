package com.codeman;

import io.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, SpringBootConfiguration.class}) // 改用自己的DateSourceConfig
@MapperScan("com.codeman.mapper")
public class ShardJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShardJdbcApplication.class, args);
	}

}
