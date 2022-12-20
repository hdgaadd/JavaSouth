package org.codeman.autoRegister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hdgaadd
 * created on 2022/12/20
 */
@Configuration
public class UserAutoConfiguration {

    @Bean
    public UserFactoryBean returnUserFactory() {
        return new UserFactoryBean();
    }
}
