package com.codem.service;

import com.codem.domain.EsProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<EsProduct, Integer> {
}
