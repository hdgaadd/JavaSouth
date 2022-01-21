package com.codeman.domain;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-28
 */
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long adminId;

    private LocalDateTime createTime;

    private String ip;

    private String browerType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBrowerType() {
        return browerType;
    }

    public void setBrowerType(String browerType) {
        this.browerType = browerType;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
        "id=" + id +
        ", adminId=" + adminId +
        ", createTime=" + createTime +
        ", ip=" + ip +
        ", browerType=" + browerType +
        "}";
    }
}
