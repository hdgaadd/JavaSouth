package com.codeman.service.impl;

import com.codeman.domain.Product;
import com.codeman.mapper.ProductMapper;
import com.codeman.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-24
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
