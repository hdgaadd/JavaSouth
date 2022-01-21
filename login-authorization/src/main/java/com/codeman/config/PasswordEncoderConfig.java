package com.codeman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder createPasswordEncoder() {
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        if (delegatingPasswordEncoder instanceof DelegatingPasswordEncoder) {
            ((DelegatingPasswordEncoder) delegatingPasswordEncoder).setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());
        }

        return delegatingPasswordEncoder;
    }
}
