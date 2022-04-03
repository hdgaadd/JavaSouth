package com.codeman.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hdgaadd
 * Created on 2022/01/09
 */
public class JWTUtil {
    private static final String SIGN = "fsdaklfj@!"; // signature签名

    /**
     * 生成token
     */
    public static String createToken(Map<String, String> message) {
        // 设置过期时间
        Calendar instance = Calendar.getInstance(); // [ˈkælɪndə(r)]日历
        instance.add(Calendar.DATE, 6);

        // 填充用户信息
        JWTCreator.Builder builder = JWT.create();
        message.forEach((k, v) -> builder.withClaim(k, v)); // [kleɪm]声明 // 添加playload载荷

        // 生成token
        String token = builder.withExpiresAt(instance.getTime()) // [ɪkˈspaɪə(r)]到期
                .sign(Algorithm.HMAC256(SIGN)); // 添加签名
        return token;
    }

    /**
     * 验证token
     */
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<String, String>(){{
            put("id", "13");
            put("name", "hdgaadd");
        }};
        System.out.println(verifyToken(createToken(map)).getClaim("name"));
    }
}
