package org.codeman.service.impl;

import org.codeman.domain.MybatisPlus;
import org.codeman.mapper.MybatisPlusMapper;
import org.codeman.service.IMybatisPlusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: MybatisPlusServiceImpl
 **/
@Repository
public class MybatisPlusServiceImpl extends ServiceImpl<MybatisPlusMapper, MybatisPlus> implements IMybatisPlusService {

}
