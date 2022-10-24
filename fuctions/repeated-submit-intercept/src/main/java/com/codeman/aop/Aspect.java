package com.codeman.aop;

import com.codeman.annotation.BanRepeatSubmit;
import com.codeman.utils.RedisLock;
import com.codeman.utils.ReturnRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author hdgaadd
 * created on 2022/03/07
 * @description 对添加防止重复提交注解，实现具体逻辑
 */
@Slf4j
@org.aspectj.lang.annotation.Aspect
@Component
public class Aspect {

    @Autowired
    private RedisLock redisLock;

    /**
     * 切点，其中@annotation里传入方法的形式参数名
     * @param banRepeatSubmit
     */
    @Pointcut("@annotation(banRepeatSubmit)")
    public void pointCut(BanRepeatSubmit banRepeatSubmit) {}

    /**
     * 环绕通知，可在方法执行前后进行拦截增强，也是与@Before的区别
     * @param proceedingJoinPoint
     * @param banRepeatSubmit
     * @return
     * @throws Throwable
     */
    @Around("pointCut(banRepeatSubmit)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, BanRepeatSubmit banRepeatSubmit) throws Throwable {
        int lockTime = banRepeatSubmit.lockTime();

        HttpServletRequest request = ReturnRequest.getRequest();
        if (request == null) {
            Assert.notNull(request, "请求不能为空");
        }

        // 把用户的唯一凭证 + 请求url，作为key，确保防重复提交针对的是同一用户的同一次url请求
        String token = request.getHeader("Authorization"); // 该Authorization为请求头里的第一个属性，存储用于验证用户身份的凭证
        String path =  request.getServletPath();
        String key = getKey(token, path);
        // 把客户端id作为值
        String value = getClientId(); // TODO: 值没有什么作用

        // 获取锁
        boolean isLock = redisLock.tryLock(key, value, lockTime);

        if (isLock) {
            log.info("获取锁成功");

            Object result;
            try {
                return proceedingJoinPoint.proceed();
            }  finally {
                redisLock.releaseLock(key, value);
                log.info("锁释放成功");
            }
        } else {
            log.error("获取锁失败，提交正在进行中，或已经提交过一次");
            return new String("获取锁失败，提交正在进行中，或已经提交过一次");
        }
    }

    /**
     *
     * @param token
     * @param path
     * @return
     */
    private String getKey(String token, String path) {

        return token + path;
    }

    private String getClientId() {
        return UUID.randomUUID().toString();
    }
}
