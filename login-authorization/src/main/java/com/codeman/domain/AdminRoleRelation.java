package com.codeman.domain;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-28
 */
public class AdminRoleRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long adminId;

    private Long roleId;


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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "AdminRoleRelation{" +
        "id=" + id +
        ", adminId=" + adminId +
        ", roleId=" + roleId +
        "}";
    }
}
