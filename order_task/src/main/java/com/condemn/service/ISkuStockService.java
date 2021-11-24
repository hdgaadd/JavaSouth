package com.condemn.service;

import com.condemn.domain.OrderTable;
import com.condemn.domain.SkuStock;
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
public interface ISkuStockService extends IService<SkuStock> {
    void updateStock(List<OrderTable> orderTableList);
}
