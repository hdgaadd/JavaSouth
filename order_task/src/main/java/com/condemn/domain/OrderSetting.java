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
public class OrderSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

    private Integer normalOrderOvertime;

    private Integer confirmOvertime;

    private Integer finishOvertime;


    public Integer getNormalOrderOvertime() {
        return normalOrderOvertime;
    }

    public void setNormalOrderOvertime(Integer normalOrderOvertime) {
        this.normalOrderOvertime = normalOrderOvertime;
    }

    public Integer getConfirmOvertime() {
        return confirmOvertime;
    }

    public void setConfirmOvertime(Integer confirmOvertime) {
        this.confirmOvertime = confirmOvertime;
    }

    public Integer getFinishOvertime() {
        return finishOvertime;
    }

    public void setFinishOvertime(Integer finishOvertime) {
        this.finishOvertime = finishOvertime;
    }

    @Override
    public String toString() {
        return "OrderSetting{" +
        "normalOrderOvertime=" + normalOrderOvertime +
        ", confirmOvertime=" + confirmOvertime +
        ", finishOvertime=" + finishOvertime +
        "}";
    }
}
