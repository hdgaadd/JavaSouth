package org.codeman;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * created on 2022/03/07
 *
 * design: Redisson分布式锁对每次提交进行加锁操作，阻止了'在上次提交的执行过程中'的重复提交
 *
 * todo: tryLock方法未获取锁没有返回false，而是继续等待，期返回false
 */
@RestController
public class OpenController {

    @Resource
    private InsideService service;

    @RequestMapping("/repeatSubmit")
    public String testSubmit() throws InterruptedException {
        return service.businessMethod();
    }

}
