package com.codeman.controller;

import com.codeman.entity.User;
import com.codeman.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Api(tags="swagger-ui测试")
@Controller
public class TestController {

    @Resource
    private UserService userService;

    @RequestMapping("/")
    @ApiOperation("测试url")
    @ResponseBody
    public User testSelect() {
        System.out.println(("----- selectAll method test ------"));
        User byId = userService.getById(1);
        System.out.println(byId);
        return byId;
    }
    /*public List<User> list(@ApiParam("查看第几页") @RequestParam int pageIndex){}*/
}
