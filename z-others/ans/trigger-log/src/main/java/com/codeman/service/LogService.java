package com.codeman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codeman.entity.Log;

public interface LogService extends IService<Log> {
    void addOperation(Log log);
}
