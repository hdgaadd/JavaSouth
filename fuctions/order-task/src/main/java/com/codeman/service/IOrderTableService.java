package com.codeman.service;

import com.codeman.domain.OrderTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-24
 */
public interface IOrderTableService extends IService<OrderTable> {

    List<OrderTable> getTimeOutOrder(int minute);

    void updateOrder(List<Integer> orderIdList);


}
