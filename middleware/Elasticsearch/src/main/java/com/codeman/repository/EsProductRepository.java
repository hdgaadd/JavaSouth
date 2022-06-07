package com.codeman.repository;

import com.codeman.entity.EsProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author hdgaadd
 * Created on 2021/12/06/21:55
 */
public interface EsProductRepository extends ElasticsearchRepository<EsProduct, Long> {
}
