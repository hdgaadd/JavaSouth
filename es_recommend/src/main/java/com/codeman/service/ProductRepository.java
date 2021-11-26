package com.codeman.service;

import com.codeman.domain.EsProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<EsProduct, Integer> {
}
