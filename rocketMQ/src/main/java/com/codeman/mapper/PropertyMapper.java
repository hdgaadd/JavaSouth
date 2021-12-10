package com.codeman.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeman.entity.Property;
import com.codeman.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyMapper extends BaseMapper<Property> {
}
