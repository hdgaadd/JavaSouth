package com.codeman.config;

import com.codeman.util.JWTUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hdgaadd
 * created on 2022/01/09
 * JWT拦截器：实现MVC拦截器的具体拦截功能
 */
public class JWTInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        try {
            JWTUtil.verifyToken(token);
            return true;
        } catch (Exception e) {
            System.out.println("验证失败");
        }
        return false;
    }
}
