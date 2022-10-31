package org.codeman.component;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/02/01
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
