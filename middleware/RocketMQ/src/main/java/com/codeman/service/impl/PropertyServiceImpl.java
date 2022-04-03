package com.codeman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeman.entity.Property;
import com.codeman.entity.User;
import com.codeman.mapper.PropertyMapper;
import com.codeman.mapper.UserMapper;
import com.codeman.service.PropertyService;
import com.codeman.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper, Property> implements PropertyService {
    @Autowired
    private PropertyMapper propertyMapper;

    @Override
    public void addMoney(int id, String name, int number) {
        Property oldProperty = propertyMapper.selectById(id); //查找原理的金钱总数
        int oldMonkey = oldProperty.getMoney();

        Property property = new Property();
        property.setId(id);
        property.setName(name);
        property.setMoney(number + oldMonkey);//增加

        propertyMapper.updateById(property);
    }
}
