package com.codeman.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeman.entity.User;
import com.codeman.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
@Service
public class TestImpl extends ServiceImpl<UserMapper, User> { //不用创建Service的接口类，也是直接使用Impl类
    @Resource
    protected UserMapper baseMapper;

    public User getById(Serializable id) {
        User user = baseMapper.selectById(id);
        user.setEmail("hello");
        return user;
    }
}
