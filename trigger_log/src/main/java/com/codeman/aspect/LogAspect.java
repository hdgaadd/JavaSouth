package com.codeman.aspect;


import com.codeman.annotation.LogOperator;
import com.codeman.entity.Log;
import com.codeman.service.LogService;
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
public class LogAspect {
    @Resource
    private LogService logService;

    @Pointcut("@annotation(com.codeman.annotation.LogOperator)")
    public void defaultMethod() {
    }

    @AfterReturning(pointcut = "defaultMethod()")
    public void operatorDataBase(JoinPoint joinPoint) throws Exception { //JoinPoint应该是切面一类的对象，保存了关于拦截的所有
        LogOperator log = getAnnotationVal(joinPoint);//获取方法上的注解对象

        Log l = new Log();
        l.setOperatorName(log.operatorName());

        logService.addOperation(l);
        System.out.println("-------------successful----------------");
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
