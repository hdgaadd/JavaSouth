package com.codeman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codeman.entity.Property;
import com.codeman.entity.User;

import java.io.Serializable;

public interface PropertyService extends IService<Property> {
     void addMoney(int id, String name, int number);
}
