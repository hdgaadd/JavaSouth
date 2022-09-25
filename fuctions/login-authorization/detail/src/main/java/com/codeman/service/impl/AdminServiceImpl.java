package com.codeman.service.impl;

import com.baomidou.mybatisplus.core.metadata.PageList;
import com.codeman.domain.Admin;
import com.codeman.domain.Permission;
import com.codeman.service.AdminService;
import com.codeman.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * Created on 2022/01/20
*/
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Admin getAdminByUsername(String name) {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setName("hdgaadd");
        return admin;
    }

    @Override
    public List<Permission> getPermissionList(Long id) {
        Permission permission = new Permission();
        permission.setValue("admin");
        List<Permission> permissions = new ArrayList<Permission>(){{
            add(permission);}};
        return permissions;
    }

    @Override
    public String getToken(String username) {
        String token = null;
        try {
            token = jwtTokenUtil.generateToken(username);
        } catch (AuthenticationException e) {
        }
        return token;
    }
}
