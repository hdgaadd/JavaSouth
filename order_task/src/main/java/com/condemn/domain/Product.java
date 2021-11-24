package com.condemn.domain;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-24
 */
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Float price;

    private Integer skuId;


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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    @Override
    public String toString() {
        return "Product{" +
        "id=" + id +
        ", name=" + name +
        ", price=" + price +
        ", skuId=" + skuId +
        "}";
    }
}
