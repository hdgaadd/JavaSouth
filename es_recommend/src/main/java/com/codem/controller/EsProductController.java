package com.codem.controller;


import com.codem.domain.EsProduct;
import com.codem.mapper.EsProductMapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/codeman/es-product")
public class EsProductController {

    @Resource
    private ElasticsearchRepository elasticsearchRepository;
    @Resource
    private EsProductMapper esProductMapper;

    @GetMapping("/save")
    public void save() {
        List<EsProduct> esProducts = esProductMapper.selectAll();
        elasticsearchRepository.save(esProducts);
    }

    @GetMapping("/search")
    public List<EsProduct> search() {
        List<EsProduct> esProducts = (List<EsProduct>) elasticsearchRepository.findAll();
        return esProducts;
    }
}

