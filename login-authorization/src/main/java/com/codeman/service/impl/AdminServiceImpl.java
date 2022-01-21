package com.codeman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codeman.domain.*;
import com.codeman.mapper.AdminMapper;
import com.codeman.mapper.ResourceMapper;
import com.codeman.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-28
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private ResourceMapper resourceMapper;


    @Override
    public Admin register(Admin admin) {
        List<Admin> list = findAdmin(admin.getName());
        if (list.size() > 0) {
            return null;
        }
        // 对密码进行加密
        String password = passwordEncoder.encode(admin.getPassword());
        System.out.println("------------密码为--------------" + password);

        admin.setPassword(password);
        adminMapper.insert(admin);
        return admin;
    }


    public List<Admin> findAdmin(String admainName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Admin::getName, admainName);
        // 不用指定类，就可以使用该list方法
        List<Admin> list = list(queryWrapper);
        return list;
    }



    @Override
    public String login(AdminLoginParam adminLoginParam) {
        if (adminLoginParam.getPassword() == null) {
            return "dnmd, 没密码还想进来";
        }
        // 根据账号密码创建UserDetail
        UserDetails userDetails = createUserDetails(adminLoginParam);

        if (userDetails.getPassword().equals(adminLoginParam.getPassword())) {
            return "token";
        } else {
            return "fail";
        }
    }

    public UserDetails createUserDetails(AdminLoginParam adminLoginParam) {
        List<Admin> adminList = findAdmin(adminLoginParam.getName());
        Admin admin = adminList.get(0);
        // 根据用户id查询所拥有的资源
        List<Resource> roleList = findAllResorce(admin.getId());
        return new AdminUserDetails(admin, roleList);
    }

    @Override
    public List<Resource> findAllResorce(Long adminId) {
        // 查询缓存
        // 添加缓存
        return resourceMapper.getResourceList(adminId);
    }
}
