package com.codeman.config;

import com.codeman.security.IUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author hdgaadd
 * Created on 2022/01/10
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 配置SpringSecurity的各种配置信息
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()/*
                .antMatchers("/r/r1").hasAnyAuthority("p1") // [ɔːˈθɒrəti]权限 // 设置访问授权服务器的客户端必须有p1权限
                */
                .anyRequest().permitAll()

                .and()
                .formLogin()

                .and()
                .logout();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return s -> {
            if ("admin".equals(s) || "user".equals(s)) {
                return new IUserDetails(s, passwordEncoder().encode(s), s);
            }
            return null;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 密码模式的管理器
     **/
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager(); // authenticationManager写成了authenticationManagerBean
    }
}
