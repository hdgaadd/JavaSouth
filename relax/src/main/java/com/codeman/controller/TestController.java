package com.codeman.controller;

import com.codeman.entity.User;
import com.codeman.mapper.UserMapper;
import com.codeman.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags="swagger-ui测试")
@RestController
public class TestController {

    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;

    @GetMapping("/")
    @ApiOperation("测试url")
    public User testSelect() {
        System.out.println(("----- selectAll method test ------"));
        System.out.println("----------------------------"+userMapper.test());
        User byId = userService.getById(1);
        System.out.println(byId);
        return byId;
    }
    /*public List<User> list(@ApiParam("查看第几页") @RequestParam int pageIndex){}*/
}
