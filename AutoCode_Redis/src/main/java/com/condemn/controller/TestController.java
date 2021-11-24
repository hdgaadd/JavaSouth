package com.condemn.controller;

import com.condemn.service.TestImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "swagger-ui测试")
@RestController
public class TestController {

    @Resource
    private TestImpl testImpl;

    @PostMapping("/getAutoCode")
    @ApiOperation("获取验证码")
    public String getAutoCode(@RequestBody String phone) {
        String autoCode = testImpl.generateAutoCode(phone);
        return autoCode; // 如果不加RestController，表示的是请求转发到一个页面里
    }

    @PostMapping("/verifyAutoCode")
    @ApiOperation("验证验证码")
    public String verifyAutoCode(@RequestParam String autoCode) {
        String result = testImpl.verifyAutoCode(autoCode);
        return result;
    }

}
