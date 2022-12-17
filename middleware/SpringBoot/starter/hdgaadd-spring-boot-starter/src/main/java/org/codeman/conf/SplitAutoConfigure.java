package org.codeman.conf;

import org.codeman.service.ISplitService;
import org.codeman.service.SplitServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hdgaadd
 * created on 2022/12/17
 */
@Configuration
@ConditionalOnClass(value = {ISplitService.class})
//@ConditionalOnBean(type = "com.alibaba.dubbo.config.spring.beans.factory.annotation.ServiceAnnotationBeanPostProcessor")
public class SplitAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    ISplitService getService() {
        return new SplitServiceImpl();
    }
}
