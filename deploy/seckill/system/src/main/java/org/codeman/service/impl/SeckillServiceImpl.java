package org.codeman.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.codeman.entity.SeckillActivity;
import org.codeman.entity.SeckillOrder;
import org.codeman.mapper.SeckillActivityMapper;
import org.codeman.mapper.SeckillOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.codeman.component.RedisService;
import org.codeman.component.RocketMQService;
import org.codeman.service.SeckillService;
import org.codeman.util.SnowFlake;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

/**
 * @author hdgaadd
 * created on 2021/12/10
 */
@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {
    @Resource
    private RedisService redisService;
    @Resource
    private SeckillActivityMapper seckillActivityMapper;
    @Resource
    private RocketMQService rocketMQService;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    private final SnowFlake snowFlake = new SnowFlake(1, 1);

    /**
     * 根据活动Id锁定库存，秒杀成功
     *
     * @param activityId
     * @param memberId
     * @return
     */
    @Override
    public Boolean skill(Long activityId, Long memberId) {
        Boolean isSeckill = false;

        // 检查该用户是否为限选用户
        Boolean isLimit = redisService.isLimitMember(activityId, memberId);
        if (!isLimit) {
            // 通过Jedis连接池锁定库存
            isSeckill = redisService.stockDeductValidator(activityId);
        } else {
            log.info("该用户已经秒杀过了，不能再秒杀");
        }
        return isSeckill;
    }

    /**
     * 秒杀成功后，创建订单
     *
     * @param activityId
     * @param memberId
     * @return
     * @throws Exception
     */
    @Override
    public String createOrder(Long activityId, Long memberId) throws Exception {
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(memberId);
        seckillOrder.setSeckillActivityId(activityId);
        seckillOrder.setCommodityId(activity.getCommodityId());
        seckillOrder.setAmount(activity.getSeckillPrice());
        seckillOrder.setOrderStatus(1);

        // id使用雪花id
        Random random = new Random(100);
        String code = String.valueOf(snowFlake.nextId() + random.nextInt(100));
        seckillOrder.setCode(code);
        log.info("订单编号为{}", code);

        // 把订单传递给消息队列，去创建订单
        return sentRocketMQ(seckillOrder) ? seckillOrder.getCode() : "订单创建失败";
    }

    /**
     * 根据订单编号支付
     *
     * @param orderCode
     * @return
     */
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

    private Boolean sentRocketMQ(SeckillOrder seckillOrder) throws Exception {
        rocketMQService.sendMessage("createOrder", JSON.toJSONString(seckillOrder));

        // 创建延迟消息，检查订单是否支付
        rocketMQService.sendMessage("pay_check", JSON.toJSONString(seckillOrder), 4);
        return true;
    }
}
