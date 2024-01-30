package org.codeman;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hdgaadd
 * created on 2022/11/05
 */
@Configuration
public class BeanConfig {

    @Bean
    public BloomFilterHelper<String> initBean() {
        // fpp误判率
        // 预期100w个数据个体插入
        return new BloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 1000000, 0.01);
    }

}
