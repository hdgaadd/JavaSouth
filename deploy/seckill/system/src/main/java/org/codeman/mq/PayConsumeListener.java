package org.codeman.mq;

import com.alibaba.fastjson.JSON;
import org.codeman.entity.SeckillOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
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
@RocketMQMessageListener(topic = "payDone", consumerGroup = "payDoneGroup")
@Slf4j
public class PayConsumeListener implements RocketMQListener<MessageExt> {
    @Resource
    private RocketMQService rocketmqService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(MessageExt messageExt) {
        try {
            String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            SeckillOrder order = JSON.parseObject(message, SeckillOrder.class);
            if (order == null) {
                log.error("该订单为null");
                return;
            }

            // 支付成功，更新数据库库存
            rocketmqService.deductStock(order.getSeckillActivityId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
