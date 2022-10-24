package com.codeman.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

/**
 * @author hdgaadd
 * created on 2022/01/10
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager; // [ɔːˌθentɪˈkeɪʃn]证实

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;


    /**
     * 配置**可访问**授权服务器的**客户端**的各项信息
     */
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() // [ˈmeməri]记忆
            .withClient("client1")
            .secret(passwordEncoder.encode("123123")) // [ˈsiːkrət]秘密
            .resourceIds("resource1")
            .authorizedGrantTypes("authorization_code",
                    "password","client_credentials","implicit","refresh_token") // 设置可访问的客户端类型
            .scopes("scope1")
            .autoApprove(false) // [əˈpruːv]同意
            .redirectUris("http://www.baidu.com")

            .and() // 再配置另一个客户端

            .withClient("client2")
            .secret(passwordEncoder.encode("123123")) // [ˈsiːkrət]秘密
            .resourceIds("resource2")
            .authorizedGrantTypes("authorization_code",
                    "password","client_credentials","implicit","refresh_token") // 设置可访问的客户端类型
            .scopes("scope2")
            .autoApprove(false) // [əˈpruːv]同意
            .redirectUris("http://www.baidu.com");
    }

    /**
     * 配置访问授权服务器的端点，即配置发放令牌的形式、配置令牌的有效期
     */
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .authorizationCodeServices(new InMemoryAuthorizationCodeServices())
                .tokenServices(tokenServices()) // 配置发放令牌的形式、配置令牌的有效期
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 访问端点的安全策略
     */
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll")
                .checkTokenAccess("permitAll")
                .allowFormAuthenticationForClients();
    }

    /**
     * 配置发放令牌的形式、配置令牌的有效期
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);
        service.setSupportRefreshToken(true);

        service.setTokenStore(tokenStore);
        service.setAccessTokenValiditySeconds(3600); // token有效期
        service.setRefreshTokenValiditySeconds(3600); // 刷新令牌有效期

        // 把普通令牌转换为JWT令牌
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);
        return service;
    }

}
