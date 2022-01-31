package com.codeman.aspect;


import com.codeman.annotation.LogOperator;
import com.codeman.entity.Log;
import com.codeman.service.LogService;
import com.codeman.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ThreadUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Resource
    private LogService logService;

    @Pointcut("@annotation(com.codeman.annotation.LogOperator)")
    public void defaultMethod() {
    }

    @AfterReturning(pointcut = "defaultMethod()")
    public void operatorDataBase(JoinPoint joinPoint) throws Exception { //JoinPoint应该是切面一类的对象，保存了关于拦截的所有
        try {
            LogOperator log = getAnnotationVal(joinPoint);//获取方法上的注解参数

            ThreadPoolUtil.getPool().execute(() -> { // 把添加日志逻辑交付给线程，实现该添加操作与本方法分割，添加操作不受本方法影响
                Log l = new Log();
                l.setOperatorName(log.operatorName());
                logService.addOperation(l);
                System.out.println("-------------successful----------------");
            });
        } catch (Exception e) {
            log.error("日志添加异常");
        }
    }

    //获取bean的注解对象
    public LogOperator getAnnotationVal(JoinPoint joinPoint) throws Exception{
        Signature signature = joinPoint.getSignature();//通过JoinPoint获取被拦截的方法
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(LogOperator.class);//获取方法上面的注解类对象
        }
        return null;
    }

}
