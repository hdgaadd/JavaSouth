package com.codeman.service.impl;

import com.codeman.domain.OrderTable;
import com.codeman.domain.SkuStock;
import com.codeman.mapper.SkuStockMapper;
import com.codeman.service.ISkuStockService;
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
