package com.codeman.mapper;

import com.codeman.domain.OrderTable;
import com.codeman.domain.SkuStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-24
 */
public interface SkuStockMapper extends BaseMapper<SkuStock> {

    void updateStock(@Param("orderTableList") List<OrderTable> orderTableList);


}
