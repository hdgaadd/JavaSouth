package com.codeman.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codeman.entity.EsProduct;
import com.codeman.entity.LearnTeacher;
import com.codeman.mapper.EsMapper;
import com.codeman.repository.EsProductRepository;
import com.codeman.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


/**
 * @author hdgaadd
 * Created on 2021/12/06 21:15:56
*/
@Service
@Slf4j
public class EsServiceImpl implements EsService {
    @Resource
    private EsMapper esProductMapper;
    @Resource
    private EsProductRepository elasticsearchRepository;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * 查询测试
     */
    @Override
    public void search() {
        // 根据字段查询
        this.searchByField();
    }

    /**
     * 根据字段查询，某数据库的数据
     *
     * BoolQueryBuilder -> SearchResourceBuilder -> SearchRequest -> SearchResponse
     *
     * TODO: 不能根据name查询，应该是因为name是Text：而why这个setIcon不生效
     */
    private void searchByField() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("id", "1"));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQueryBuilder)
                .size(3);
        // new Sting[]指定所查询的数据库
        SearchRequest searchRequest = new SearchRequest(new String[]{"esteacher"}, searchSourceBuilder);

        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            if (hits.getHits() != null && hits.getHits().length > 0) {
                log.info(JSONObject.parseObject(hits.getHits()[0].getSourceAsString(), EsProduct.class).toString());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("-----------------------------------------------------------------------------------");
    }

    /**
     * ik分词器查询
     *
     * SearchResourceBuilder -> SearchRequest -> SearchResponse
     *
     * @return
     */
    @Override
    public List<EsProduct> selectMatch(String key, String value) throws IOException {
        // 实例化SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchQuery(key, value));
        // 把SearchSourceBuilder添加进SearchRequest
        SearchRequest searchRequest = new SearchRequest()
                .source(searchSourceBuilder);

        // 查询，返回SearchResponse
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 把SearchResponse转换为EsProduct
        List<EsProduct> esProducts = new ArrayList<>();
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            EsProduct esProduct = JSONObject.parseObject(hit.getSourceAsString(), EsProduct.class);
            esProducts.add(esProduct);
        }
        return esProducts;
    }

























    @Override
    public int importAll() {
        List<LearnTeacher> teacherList = esProductMapper.selectAll();
        List<EsProduct> productList = new ArrayList<>();

        for (LearnTeacher learnTeacher : teacherList) {
            for (int i = 0; i < learnTeacher.getFansCount(); i++) {
                EsProduct esProduct = new EsProduct();
                esProduct.setId((long) UUID.randomUUID().toString().hashCode());
                esProduct.setName(learnTeacher.getName());
                esProduct.setIcon(learnTeacher.getName()); // 不生效，why?
                productList.add(esProduct);
            }
        }
        Iterator<EsProduct> iterator = elasticsearchRepository.saveAll(productList).iterator();
        int ret = 0;
        while (iterator.hasNext()) {
            iterator.next();
            ret++;
        }
        return ret;
    }
}
