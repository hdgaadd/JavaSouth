package org.codeman.mq;

import com.alibaba.fastjson.JSON;
import org.codeman.entity.SeckillOrder;
import org.codeman.mapper.SeckillOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.codeman.component.RedisService;
import org.codeman.service.RocketMQService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author hdgaadd
 * created on 2021/12/12
 */
@Component
@RocketMQMessageListener(topic = "createOrder", consumerGroup = "orderGroup")
@Slf4j
public class OrderConsumeListener implements RocketMQListener<MessageExt> {
    @Resource
    private SeckillOrderMapper seckillOrderMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private RocketMQService rocketmqService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(MessageExt messageExt) {
        try {
            String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            log.info("接收到创建订单消息{}", message);
            SeckillOrder order = JSON.parseObject(message, SeckillOrder.class);
            if (order == null) {
                log.error("该订单为null");
                return;
            }

            // 更新数据库，限定库存 + 1，可用库存 - 1
            int result =  rocketmqService.updateOrder(order.getSeckillActivityId());
            if (result > 0) {
                log.info("更新数据库，限定库存+1，可用库存-1成功");

                seckillOrderMapper.insert(order);
                log.info("创建订单成功");

                // 把该活动id+用户id作为key，加入到Jedis连接池里，作为限选用户
                redisService.addLimitUser(order.getSeckillActivityId(), order.getUserId());
                log.info("添加限选用户成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
