package com.codeman.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codeman.component.RedisService;
import com.codeman.domain.SeckillActivity;
import com.codeman.domain.SeckillOrder;
import com.codeman.mapper.SeckillActivityMapper;
import com.codeman.mapper.SeckillOrderMapper;
import com.codeman.service.RocketmqService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.mapping.model.PreferredConstructorDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import util.LOG;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author hdgaadd
 * Created on 2021/12/12
 */
@Component
@RocketMQMessageListener(topic = "pay_check", consumerGroup = "payCheckGroup")
public class PayCheckConsumeListener implements RocketMQListener<MessageExt> {
    @Resource
    private RocketmqService rocketmqService;
    @Resource
    private RedisService redisService;
    @Resource
    protected SeckillOrderMapper seckillOrderMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(MessageExt messageExt) {
        try {
            LOG.log("检查订单支付状态开始");
            String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            SeckillOrder oldOrder = JSON.parseObject(message, SeckillOrder.class);
            if (oldOrder == null) {
                LOG.log("该订单为null");
                return;
            }
            // 根据先前的订单编号，获取此时最新的该订单，或许该订单已经支付更新状态了
            SeckillOrder order = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("code", oldOrder.getCode()));
            LOG.log("订单的状态为", order.getOrderStatus());
            if (order.getOrderStatus() != 2) {
                order.setOrderStatus(3);
                // 更新订单状态
                seckillOrderMapper.updateById(order);
                // 订单超时，更新数据库库存
                rocketmqService.revertDataBase(order);
                // 订单超时，更新Redis库存
                redisService.revertStock(order);
                // 将用户从锁定状态解除
                redisService.removeLimitMember(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
