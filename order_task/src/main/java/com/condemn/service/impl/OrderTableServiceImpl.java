package com.condemn.service.impl;

import com.condemn.domain.OrderTable;
import com.condemn.mapper.OrderTableMapper;
import com.condemn.mapper.SkuStockMapper;
import com.condemn.service.IOrderTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-24
 */
@Service
public class OrderTableServiceImpl extends ServiceImpl<OrderTableMapper, OrderTable> implements IOrderTableService {

    @Resource OrderTableMapper orderTableMapper;
    @Resource
    SkuStockMapper skuStockMapper;

    @Override
    public List<OrderTable> getTimeOutOrder(int minute) {

        return orderTableMapper.getTimeOutOrder(minute);
    }

    @Override
    public void updateOrder(List<Integer> orderIdList) {
        orderTableMapper.updateOrder(orderIdList);
    }


}
