package org.codeman.service;

import org.codeman.domain.MybatisPlus;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: IMybatisPlusService
 **/
public interface IMybatisPlusService extends IService<MybatisPlus> {
    void lambdaQueryTest();
}
