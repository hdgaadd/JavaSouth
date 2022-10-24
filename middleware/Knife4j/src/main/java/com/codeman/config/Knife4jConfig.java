package com.codeman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author hdgaadd
 * created on 2022/03/16
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description("Knife4j文档介绍")
                        .termsOfServiceUrl("https://www.xx.com/")
                        .contact(new Contact("abc", "https://www.example.com", "abc@qq.com"))
                        .version("1.0")
                        .build())
                // 分组名称 TODO: ??
                .groupName("2.X版本")
                .select()
                // 指定controller路径
                .apis(RequestHandlerSelectors.basePackage("com.codeman.controller"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }
}
