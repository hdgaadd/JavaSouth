package com.codeman.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codeman.domain.SeckillActivity;
import com.codeman.domain.SeckillOrder;
import com.codeman.mapper.SeckillActivityMapper;
import com.codeman.mapper.SeckillOrderMapper;
import com.codeman.component.RocketMQService;
import com.codeman.service.SeckillActivityService;
import org.springframework.stereotype.Service;
import util.LOG;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author hdgaadd
 * Created on 2021/12/13
*/
@Service
public class SeckillActivityServiceImpl implements SeckillActivityService {
    @Resource
    private SeckillActivityMapper seckillActivityMapper;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;
    @Resource
    private RocketMQService rocketMQService;

    @Override
    public SeckillActivity getSeckillActivity(String orderCode) {
        // 根据订单编号查询订单
        QueryWrapper<SeckillOrder> seckillOrderQueryWrapper = new QueryWrapper<>();
        seckillOrderQueryWrapper.eq("code", orderCode);
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(seckillOrderQueryWrapper);
        Long id = seckillOrder.getSeckillActivityId();
        SeckillActivity result = seckillActivityMapper.selectById(id);
        return result;
    }

    @Override
    public String payOrder(String orderCode) throws Exception {
        // 根据订单编号查询订单
        QueryWrapper<SeckillOrder> seckillOrderQueryWrapper = new QueryWrapper<>();
        seckillOrderQueryWrapper.eq("code", orderCode);
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(seckillOrderQueryWrapper);
        if (seckillOrder == null) {
            return "无此订单";
        }
        if (seckillOrder.getOrderStatus() == 0) {
            return "订单无效";
        }
        // 暂定支付成功
        seckillOrder.setOrderStatus(2);
        Date currDate = new Date();
        LocalDateTime time = currDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        seckillOrder.setPayTime(time);
        // 更新订单
        seckillOrderMapper.updateById(seckillOrder);
        // 传输订单给消息接收者
        rocketMQService.sendMessage("payDone", JSON.toJSONString(seckillOrder));
        return "支付成功";
    }
}
