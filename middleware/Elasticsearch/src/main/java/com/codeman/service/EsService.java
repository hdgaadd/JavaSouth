package com.codeman.service;

import com.codeman.entity.EsProduct;

import java.io.IOException;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2021/12/06 21:15:56
*/
public interface EsService {
    /**
     * 导入
     *
     * @return
     */
    int importAll();

    /**
     * ik分词器查询
     * @return
     */
    List<EsProduct> selectMatch(String key, String value) throws IOException;

    /**
     * 查询测试
     */
    void search();
}