package com.codeman.service;

import com.codeman.domain.SeckillOrder;
import org.springframework.stereotype.Service;

/**
 * @author hdgaadd
 * Created on 2021/12/14
 */
public interface RocketmqService {
    /**
     * 订单超时，恢复数据库库存
     * @param order
     */
    void revertDataBase(SeckillOrder order);

    /**
     * 创建订单，更新数据库，限定库存+1，可用库存-1
     * @param seckillActivityId
     * @return
     */
    int updateOrder(Long seckillActivityId);

    /**
     * 支付成功，更新数据库库存
     * @param seckillActivityId
     * @return
     */
    void deductStock(Long seckillActivityId);
}
