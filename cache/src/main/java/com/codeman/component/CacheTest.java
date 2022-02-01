package com.codeman.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hdgaadd
 * Created on 2022/02/01
 */
@Component
@EnableCaching
public class CacheTest {

    @Cacheable("cacheIndex") // 使用一个名为cacheIndex的Cache对象进行缓存
    public String test(Integer index) {
        System.out.println("---------------------------第一次使用该方法---------------------------");
        return "the return result is " + index;
    }
}
