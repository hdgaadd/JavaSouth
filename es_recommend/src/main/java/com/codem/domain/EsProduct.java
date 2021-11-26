package com.codem.domain;


import java.io.Serializable;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-26
 */

public class EsProduct implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;

    private String name;

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
