package com.condemn.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.condemn.entity.User;

public interface UserMapper extends BaseMapper<User> {
    Integer test();
}
