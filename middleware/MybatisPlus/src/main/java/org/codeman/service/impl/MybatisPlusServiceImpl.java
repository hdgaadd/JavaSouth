package org.codeman.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.codeman.domain.MybatisPlus;
import org.codeman.mapper.MybatisPlusMapper;
import org.codeman.service.IMybatisPlusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: MybatisPlusServiceImpl
 **/
@Slf4j
@Repository
public class MybatisPlusServiceImpl extends ServiceImpl<MybatisPlusMapper, MybatisPlus> implements IMybatisPlusService {

    public void lambdaQueryTest() {
        List<MybatisPlus> list = lambdaQuery().eq(MybatisPlus::getId, 1).select(MybatisPlus::getName).list();
        log.info(list.toString());
    }
}
