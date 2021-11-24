package com.condemn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.condemn.entity.User;
import com.condemn.mapper.UserMapper;
import com.condemn.service.UserService;
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
