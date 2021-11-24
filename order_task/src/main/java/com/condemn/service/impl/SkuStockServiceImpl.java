package com.condemn.service.impl;

import com.condemn.domain.OrderTable;
import com.condemn.domain.SkuStock;
import com.condemn.mapper.SkuStockMapper;
import com.condemn.service.ISkuStockService;
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
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements ISkuStockService {
    @Resource
    private SkuStockMapper skuStockMapper;
    @Override
    public void updateStock(List<OrderTable> orderTableList) {
        skuStockMapper.updateStock(orderTableList);
    }
}
