package org.codeman;

import org.codeman.component.BanRepeatSubmit;
import org.springframework.stereotype.Service;

/**
 * @author hdgaadd
 * created on 2022/10/30
 */
@Service
public class InsideService {

    @BanRepeatSubmit(lockTime = 6)
    public String businessMethod() throws InterruptedException {
        // 线程睡眠，模拟业务执行
        Thread.sleep(3000);
        return "successful!";
    }
}
