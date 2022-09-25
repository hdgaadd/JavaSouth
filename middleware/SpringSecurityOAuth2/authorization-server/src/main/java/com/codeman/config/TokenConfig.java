package com.codeman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author hdgaadd
 * Created on 2022/01/10
 */
@Configuration
public class TokenConfig {

    /**
     * 设置token的类型
     **/
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter()); // 设置token类型为JWT
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 设置JWT的签名，即计算公式
        jwtAccessTokenConverter.setSigningKey("hdgaadd");
        return jwtAccessTokenConverter;
    }
}
