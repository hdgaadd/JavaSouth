package org.codeman.conf;

import org.learn.LearnAutoConfigureBefore;
import org.codeman.service.ISplitService;
import org.codeman.service.SplitServiceImpl;
import org.learn.LearnConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author hdgaadd
 * created on 2022/12/17
 */

// @ConditionalOnBean(type = "com.alibaba.dubbo.config.spring.beans.factory.annotation.ServiceAnnotationBeanPostProcessor")
@ConditionalOnClass(value = {ISplitService.class})

// 规定引用本依赖的启动yml中，learn-properties.enable必须为true，否则本配置不进行注册
@EnableConfigurationProperties(LearnConfigProperties.class)
@ConditionalOnProperty(name = "learn-properties.enable", havingValue = "true")

// 指定要扫描的类路径，不指定默认是启动类所在目录，即org.codeman
@ComponentScan(basePackages = {
        "org.codeman",
        "org.learn"
})

// 在LearnAutoConfigureBefore之前执行本配置类：A配置类所注册的bean是LearnAutoConfigureBefore所依赖的资源，可使用以下
@AutoConfigureBefore(LearnAutoConfigureBefore.class)

@Configuration
public class SplitAutoConfigure {

    static {
        System.out.println("SplitAutoConfigure is start!");
    }

    @Bean
    @ConditionalOnMissingBean
    ISplitService getService() {
        return new SplitServiceImpl();
    }
}
