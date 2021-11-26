package com.codem.mapper;

import com.codem.domain.EsProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-26
 */
public interface EsProductMapper extends BaseMapper<EsProduct> {

    List<EsProduct> selectAll();
}
