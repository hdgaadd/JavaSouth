package com.codeman.service;


import com.codeman.domain.EsProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductRepository extends ElasticsearchRepository<List<EsProduct>, Integer> {
}
