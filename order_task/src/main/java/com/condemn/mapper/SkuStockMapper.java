package com.condemn.mapper;

import com.condemn.domain.OrderTable;
import com.condemn.domain.SkuStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
public interface SkuStockMapper extends BaseMapper<SkuStock> {

    void updateStock(@Param("orderTableList") List<OrderTable> orderTableList);


}
