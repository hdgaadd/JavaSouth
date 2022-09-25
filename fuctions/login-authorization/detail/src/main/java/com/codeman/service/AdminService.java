package com.codeman.service;

import com.codeman.domain.Admin;
import com.codeman.domain.Permission;

import java.util.List;

/**
 * @author hdgaadd
 * Created on 2022/01/20
 */
public interface AdminService {
    /**
     * 根据name获取Admin实体类
     */
    Admin getAdminByUsername(String name);

    /**
     * 根据用户id获取权限列表
     */
    List<Permission> getPermissionList(Long id);

    /**
     * 登录获取token
     */
    String getToken(String username);
}