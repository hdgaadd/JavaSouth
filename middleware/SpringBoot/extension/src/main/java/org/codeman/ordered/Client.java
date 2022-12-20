package org.codeman.ordered;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hdgaadd
 * created on 2022/12/17
 *
 * description:
 * 	- 策略模式拥有各类实现类，Ordered实现实现类的调用排序
 * 	- ApplicationRunner非SPI，在Spring容器启动后才运行
 */
@SpringBootApplication
public class Client {

	public static void main(String[] args) {
		SpringApplication.run(Client.class, args);
	}

}
