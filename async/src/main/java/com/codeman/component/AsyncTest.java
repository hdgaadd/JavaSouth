package com.codeman.component;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author hdgaadd
 * Created on 2022/02/01
 */
@Component
public class AsyncTest {

    @Async
    public void asyncTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1); // 让主线程先输出结果，若主线程先输出，则表明方法以下的代码，不用等待方法执行后再执行
        System.out.println("--------------------------the method thread executed--------------------------");
    }
}
