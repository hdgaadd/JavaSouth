package org.codeman.component;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author hdgaadd
 * created on 2022/03/07
 */
@Aspect
@Component
@Slf4j
public class SubmitAspect {

    @Resource
    private RedisLock redisLock;

    private int executeCount = 0;

    /**
     * 切点，其中@annotation里传入方法的形式参数名
     *
     * @param banRepeatSubmit
     */
    @Pointcut("@annotation(banRepeatSubmit)")
    public void pointCut(BanRepeatSubmit banRepeatSubmit) {}

    /**
     * 环绕通知，可在方法执行前后进行拦截增强，也是与@Before的区别
     *
     * @param proceedingJoinPoint
     * @param banRepeatSubmit
     * @return
     * @throws Throwable
     */
    @Around("pointCut(banRepeatSubmit)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, BanRepeatSubmit banRepeatSubmit) throws Throwable {
        int lockTime = banRepeatSubmit.lockTime();

        HttpServletRequest request = this.getRequest();
        // 把用户的唯一凭证 + 请求url，作为key，确保防重复提交针对的是同一用户的同一次url请求
        // 该Authorization为请求头里的第一个属性，存储用于验证用户身份的凭证
        String key = request.getHeader("Authorization") + request.getServletPath();
        // 把客户端id作为值
        String value = getClientId();

        // 获取锁
        boolean isLock = redisLock.tryLock(key, value, lockTime);
        String executeMsg = String.format("第%s次执行: ", ++executeCount);

        if (isLock) {
            log.info(executeMsg + "获取锁成功");
            try {
                // 获取锁，则执行业务逻辑
                return proceedingJoinPoint.proceed();
            }  finally {
                redisLock.releaseLock(key, value);
                log.info(executeMsg + "释放锁成功");
            }
        } else {
            log.error("获取锁失败，上次提交正在进行中");
            return null;
        }
    }

    private String getClientId() {
        return UUID.randomUUID().toString();
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }
}
