package org.codeman.service;

/**
 * @author hdgaadd
 * created on 2021/12/10
*/
public interface SeckillService {
    /**
     * 根据活动Id锁定库存，秒杀成功
     *
     * @param activityId
     * @param memberId
     * @return
     */
    Boolean skill(Long activityId, Long memberId);

    /**
     * 秒杀成功后，创建订单
     *
     * @param activityId
     * @param memberId
     * @return
     * @throws Exception
     */
    String createOrder(Long activityId, Long memberId) throws Exception;

    /**
     * 根据订单编号支付
     *
     * @param orderCode
     * @return
     */
    String payOrder(String orderCode) throws Exception;
}