package com.codeman.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hdgaadd
 * created on 2022/01/09
 * 创建MVC拦截器，拦截特定url使用JWT进行验证
 */
@Configuration
public class MVCInterceptor implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/test/needVerifycation") // 配置拦截的url
                .excludePathPatterns("/test/getToken"); // [ɪkˈskluːd]不包括，配置不拦截的url
    }
}
