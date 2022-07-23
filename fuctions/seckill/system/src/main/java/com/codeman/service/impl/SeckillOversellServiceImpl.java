package com.codeman.service.impl;

import com.alibaba.fastjson.JSON;
import com.codeman.component.RedisService;
import com.codeman.domain.SeckillActivity;
import com.codeman.domain.SeckillOrder;
import com.codeman.mapper.SeckillActivityMapper;
import com.codeman.component.RocketMQService;
import com.codeman.service.SeckillOversellService;
import com.codeman.util.SnowFlake;
import org.springframework.stereotype.Service;
import util.LOG;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author hdgaadd
 * Created on 2021/12/10 00:00:11
 */
@Service
public class SeckillOversellServiceImpl implements SeckillOversellService {
    @Resource
    private RedisService redisService;
    @Resource
    private SeckillActivityMapper seckillActivityMapper;
    @Resource
    private RocketMQService rocketMQService;

    private final SnowFlake snowFlake = new SnowFlake(1, 1);

    @Override
    public Boolean stockDeductVaildator(Long activityId, Long memberId) {
        // 检查该用户是否为限选用户
        Boolean isLimit = redisService.isLimitMember(activityId, memberId);
        Boolean isSeckill = false;
        if (!isLimit) {
            // 通过Jedis连接池锁定库存
            isSeckill = redisService.stockDeductVaildator(activityId);
        } else {
            LOG.log("该用户已经秒杀过了，不能再秒杀");
        }
        return isSeckill;
    }

    /**
     * 秒杀成功后，创建订单
     * @param activityId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public String createOrder(Long activityId, Long userId) throws Exception {
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        SeckillOrder seckillOrder = new SeckillOrder();
        // id使用雪花id
        Random random = new Random(100);
        String code = String.valueOf(snowFlake.nextId() + random.nextInt(100));
        seckillOrder.setCode(code);
        LOG.log("订单编号为", code);
        seckillOrder.setUserId(userId);
        seckillOrder.setSeckillActivityId(activityId);
        seckillOrder.setCommodityId(activity.getCommodityId());
        seckillOrder.setAmount(activity.getSeckillPrice());
        seckillOrder.setOrderStatus(1);
        // 把订单传递给消息队列，去创建订单
        Boolean result = sentRocketMQ(seckillOrder);
        return result ? seckillOrder.getCode() : "订单创建失败";
    }

    private Boolean sentRocketMQ(SeckillOrder seckillOrder) throws Exception {
        rocketMQService.sendMessage("createOrder", JSON.toJSONString(seckillOrder));
        // 创建延迟消息，检查订单是否支付
        rocketMQService.sendMessage("pay_check", JSON.toJSONString(seckillOrder), 4);
        return true;
    }
}
