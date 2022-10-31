package com.codeman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeman.entity.User;
import com.codeman.mapper.UserMapper;
import com.codeman.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    @Autowired
    protected UserMapper baseMapper;

    public User getById(Serializable id) {
        User user = baseMapper.selectById(id);
        return user;
    }
}
