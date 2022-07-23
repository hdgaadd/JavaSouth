package com.codeman.controller;

import com.codeman.domain.SeckillActivity;
import com.codeman.service.SeckillActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.LOG;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * Created on 2021/12/13
 */
@Api(tags = "支付")
@RestController
@RequestMapping("/activity")
public class SeckillActivityController {
    @Resource
    private SeckillActivityService seckillActivityService;

    @GetMapping("/findActivity/{orderCode}")
    @ApiOperation("根据订单编号查询活动")
    public SeckillActivity getSeckillActivity(@PathVariable String orderCode) {
        LOG.log("IDEA20真牛逼😅, IDEA19yyds");
        SeckillActivity seckillActivity = seckillActivityService.getSeckillActivity(orderCode);
        return seckillActivity;
    }

    @GetMapping("payOrder/{orderCode}")
    @ApiOperation("支付订单")
    public String payOrder(@PathVariable String orderCode) throws Exception {
        String result = seckillActivityService.payOrder(orderCode);
        return result;
    }
}
