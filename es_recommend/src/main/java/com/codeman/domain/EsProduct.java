package com.codeman.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.codeman.constant.FieldAnalyzer;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-26
 */
@Document(indexName = "product", shards = 1, replicas = 0, refreshInterval = "-1")
public class EsProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Field(analyzer = FieldAnalyzer.IK_MAX_WORD, type = FieldType.Text)
    private String name;

    @Field(analyzer = FieldAnalyzer.IK_MAX_WORD, type = FieldType.Text)
    private Integer saleNum;

    private Integer promotion;

    private Integer stock;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getPromotion() {
        return promotion;
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "EsProduct{" +
                "id=" + id +
                ", name=" + name +
                ", salenum=" + saleNum +
                ", promotion=" + promotion +
                ", stock=" + stock +
                "}";
    }
}
