package com.codeman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codeman.entity.User;

import java.io.Serializable;

public interface UserService extends IService<User> {
     User getById(Serializable id);
}
