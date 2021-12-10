package com.codeman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeman.entity.Log;
import com.codeman.mapper.LogMapper;
import com.codeman.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServcieImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public void addOperation(Log log) {
        logMapper.insert(log);
    }
}
