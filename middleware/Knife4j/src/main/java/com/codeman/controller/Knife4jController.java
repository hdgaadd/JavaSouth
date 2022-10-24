package com.codeman.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hdgaadd
 * created on 2022/03/16
 */
@Api(tags = "Knife4j-test")
@RestController
public class Knife4jController { // http://localhost:8080/doc.html

    @ApiImplicitParam(name = "name", value = "姓名", required = true) // 参数描述
    @ApiOperation(value = "方法描述")
    @RequestMapping("/Knife4j-test")
    // 该value中的数据代表url传递的参数名称，如https://localhost/test/?name=name
    // value的值不与方面参数名一致，会报错java.lang.NullPointException
    public String test(@RequestParam(value = "name") String name) {
        return name + " say:" + "hello, Knife4j";
    }

}
