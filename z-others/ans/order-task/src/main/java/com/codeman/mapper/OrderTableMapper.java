package com.codeman.mapper;

import com.codeman.domain.OrderTable;
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
public interface OrderTableMapper extends BaseMapper<OrderTable> {

    List<OrderTable> getTimeOutOrder(@Param("minute") int minute);

    void updateOrder(@Param("orderIdList") List<Integer> orderIdList);



}
