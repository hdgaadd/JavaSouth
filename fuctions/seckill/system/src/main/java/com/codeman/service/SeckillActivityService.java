package com.codeman.service;

import com.codeman.domain.SeckillActivity;

/**
 * @author hdgaadd
 * created on 2021/12/13
*/
public interface SeckillActivityService {
    /**
     * 根据订单编号查询活动
     * @param orderCode
     * @return
     */
    SeckillActivity getSeckillActivity(String orderCode);

    /**
     * 根据订单编号支付
     * @param orderCode
     * @return
     */
    String payOrder(String orderCode) throws Exception;
}