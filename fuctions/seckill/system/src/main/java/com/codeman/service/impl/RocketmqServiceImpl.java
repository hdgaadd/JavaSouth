package com.codeman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codeman.domain.SeckillActivity;
import com.codeman.domain.SeckillOrder;
import com.codeman.mapper.SeckillActivityMapper;
import com.codeman.service.RocketmqService;
import org.springframework.stereotype.Service;
import util.LOG;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * Created on 2021/12/14
 */
@Service
public class RocketmqServiceImpl implements RocketmqService {
    @Resource
    private SeckillActivityMapper seckillActivityMapper;

    @Override
    public void revertDataBase(SeckillOrder order) {
        SeckillActivity seckillActivity = seckillActivityMapper.selectById(order.getSeckillActivityId());
        seckillActivity.setLockStock(seckillActivity.getLockStock() - 1);
        seckillActivityMapper.update(seckillActivity, null);
        LOG.log("恢复数据库成功");
    }

    @Override
    public int updateOrder(Long seckillActivityId) {
        QueryWrapper<SeckillActivity> seckillOrderQueryWrapper = new QueryWrapper<>();
        seckillOrderQueryWrapper.eq("id", seckillActivityId);
        SeckillActivity activity = seckillActivityMapper.selectById(seckillActivityId);
        activity.setLockStock(activity.getLockStock() + 1);
        int result = seckillActivityMapper.update(activity, seckillOrderQueryWrapper);
        return result;
    }

    @Override
    public void deductStock(Long seckillActivityId) {
        SeckillActivity activity = seckillActivityMapper.selectById(seckillActivityId);
        activity.setLockStock(activity.getLockStock() - 1);
        activity.setAvailableStock(activity.getAvailableStock() - 1);
        seckillActivityMapper.update(activity, null);
        LOG.log("支付成功，更新数据库库存");
    }
}
