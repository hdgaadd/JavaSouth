package com.condemn.controller;


import com.condemn.domain.OrderTable;
import com.condemn.mapper.OrderTableMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-24
 */
@RestController
@RequestMapping("/codeman/order-table")
public class OrderTableController {
    @Resource
    private OrderTableMapper orderMapper;

    @GetMapping("/")
    @ApiOperation("测试url")
    public OrderTable testSelect() {
        /*System.out.println(("----- selectAll method test ------"));
        Order order = orderMapper.selectById(1);
        return order;*/
        return orderMapper.selectById(1);
    }
}

