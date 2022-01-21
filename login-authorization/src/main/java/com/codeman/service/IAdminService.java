package com.codeman.service;

import com.codeman.domain.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.codeman.domain.AdminLoginParam;
import com.codeman.domain.Resource;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-28
 */
public interface IAdminService extends IService<Admin> {
    /**
     * 注册
     * @param admin
     * @return
     */
    Admin register(Admin admin);

    /**
     * 登录
     * @param adminLoginParam
     * @return
     */
    String login(AdminLoginParam adminLoginParam);

    /**
     * 根据账号密码创建UserDetail
     * @param adminLoginParam
     * @return
     */
    UserDetails createUserDetails(AdminLoginParam adminLoginParam);

    /**
     * 根据用户name查询出用户实体类
     * @param admainName
     * @return
     */
    List<Admin> findAdmin(String admainName);

    /**
     * 根据用户id查询所所拥有的资源
     * @param adminId
     * @return
     */
    List<Resource> findAllResorce(Long adminId);
}
