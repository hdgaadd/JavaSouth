package com.codeman.config;

import com.codeman.domain.Admin;
import com.codeman.domain.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * created on 2022/01/20
 */
public class AdminUserDetails implements UserDetails {
    private Admin admin;
    private List<Permission> permissions;

    public AdminUserDetails(Admin admin, List<Permission> permissions) {
        this.admin = admin;
        this.permissions = permissions;
    }

    /**
     * 返回用户权限
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .filter(permission -> permission.getValue() != null)
                // 转换为SimpleGrantedAuthority
                .map(permission -> new SimpleGrantedAuthority(permission.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getName();
    } // error所在

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; // admin.getStatus().equals(1)登录的用户的状态是可用才可登录
    }
}
