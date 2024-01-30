package org.codeman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.codeman.entity.SeckillActivity;
import org.codeman.entity.SeckillOrder;
import org.codeman.mapper.SeckillActivityMapper;
import lombok.extern.slf4j.Slf4j;
import org.codeman.service.RocketMQService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * created on 2021/12/14
 */
@Service
@Slf4j
public class RocketMQServiceImpl implements RocketMQService {
    @Resource
    private SeckillActivityMapper seckillActivityMapper;

    /**
     * 创建订单，更新数据库，限定库存 + 1
     *
     * @param actId
     * @return
     */
    @Override
    public int updateOrder(Long actId) {
        QueryWrapper<SeckillActivity> seckillOrderQueryWrapper = new QueryWrapper<>();
        seckillOrderQueryWrapper.eq("id", actId);
        SeckillActivity activity = seckillActivityMapper.selectById(actId);
        activity.setLockStock(activity.getLockStock() + 1);
        return seckillActivityMapper.update(activity, seckillOrderQueryWrapper);
    }

    /**
     * 订单超时，恢复数据库库存
     *
     * @param order
     */
    @Override
    public void revertDataBase(SeckillOrder order) {
        SeckillActivity seckillActivity = seckillActivityMapper.selectById(order.getSeckillActivityId());
        seckillActivity.setLockStock(seckillActivity.getLockStock() - 1);
        seckillActivityMapper.update(seckillActivity, null);
        log.info("恢复数据库成功");
    }

    /**
     * 支付成功，限定库存 - 1，可用库存 - 1
     *
     * @param actId
     * @return
     */
    @Override
    public void deductStock(Long actId) {
        SeckillActivity activity = seckillActivityMapper.selectById(actId);
        activity.setLockStock(activity.getLockStock() - 1);
        activity.setAvailableStock(activity.getAvailableStock() - 1);
        seckillActivityMapper.update(activity, null);
        log.info("支付成功，更新数据库库存");
    }
}
