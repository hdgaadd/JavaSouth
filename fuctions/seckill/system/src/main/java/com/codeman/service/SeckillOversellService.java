package com.codeman.service;

import com.codeman.domain.Member;

/**
 * @author hdgaadd
 * created on 2021/12/10 00:00:11
*/
public interface SeckillOversellService {
    /**
     * 根据活动Id锁定库存，秒杀成功
     * @param activityId
     * @return
     */
    Boolean stockDeductVaildator(Long activityId, Long memberId);

    /**
     * 秒杀成功后，创建订单
     * @param activityId
     * @param memberId
     * @return
     */
    String createOrder(Long activityId, Long memberId) throws Exception;
}