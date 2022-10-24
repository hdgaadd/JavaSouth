package com.codeman.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.codeman.component.RedisService;
import com.codeman.domain.SeckillActivity;
import com.codeman.domain.SeckillOrder;
import com.codeman.mapper.SeckillActivityMapper;
import com.codeman.mapper.SeckillOrderMapper;
import com.codeman.service.RocketmqService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import util.LOG;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author hdgaadd
 * created on 2021/12/12
 */
@Component
@RocketMQMessageListener(topic = "createOrder", consumerGroup = "orderGroup")
public class OrderConsumeListener implements RocketMQListener<MessageExt> {
    @Resource
    private SeckillOrderMapper seckillOrderMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private RocketmqService rocketmqService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(MessageExt messageExt) {
        try {
            String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            LOG.log("接收到消息");
            SeckillOrder order = JSON.parseObject(message, SeckillOrder.class);
            if (order == null) {
                LOG.log("该订单为null");
                return;
            }
            // 更新数据库，限定库存+1，可用库存-1
            int result =  rocketmqService.updateOrder(order.getSeckillActivityId());
            if (result > 0) {
                LOG.log("更新数据库，限定库存+1，可用库存-1成功");

                seckillOrderMapper.insert(order);
                LOG.log("创建订单成功");

                // 把该活动id+用户id作为key，加入到Jedis连接池里，作为限选用户
                Boolean ret =  redisService.addLimitUser(order.getSeckillActivityId(), order.getUserId());
                LOG.log("添加限选用户成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
