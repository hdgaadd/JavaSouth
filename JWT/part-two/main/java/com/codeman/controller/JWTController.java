package com.codeman.controller;

import com.codeman.entity.User;
import com.codeman.service.UserService;
import com.codeman.util.JWTUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController // 加上get方法可以不添加@RequestBody
@RequestMapping("/test")
@CrossOrigin
public class JWTController {
    @Resource
    private UserService userService;

    @GetMapping("/getToken")
    public String getToken() {
        HashMap<String, String> map = new HashMap<String, String>(){{
            put("id", "13");
            put("name", "hdgaadd");
        }};
        String token = JWTUtil.createToken(map);
        return token;
    }

    /**
     * 需要token验证的url
     */
    @PostMapping("/needVerifycation")
    public User needVerifycation() {
        User user = userService.getById(1);
        return user;
    }
}
