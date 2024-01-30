package org.codeman.service;

import org.codeman.entity.SeckillOrder;

/**
 * @author hdgaadd
 * created on 2021/12/14
 */
public interface RocketMQService {

    /**
     * 创建订单，更新数据库，限定库存 + 1
     *
     * @param seckillActivityId
     * @return
     */
    int updateOrder(Long seckillActivityId);

    /**
     * 订单超时，恢复数据库库存
     *
     * @param order
     */
    void revertDataBase(SeckillOrder order);

    /**
     * 支付成功，限定库存 - 1，可用库存 - 1
     *
     * @param seckillActivityId
     * @return
     */
    void deductStock(Long seckillActivityId);
}
