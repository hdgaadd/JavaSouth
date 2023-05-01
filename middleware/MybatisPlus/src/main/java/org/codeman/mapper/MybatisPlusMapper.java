package org.codeman.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.codeman.domain.MybatisPlus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @ClassName: MybatisPlusMapper
 **/
@Mapper
public interface MybatisPlusMapper extends BaseMapper<MybatisPlus> {
    @Insert("INSERT INTO `universe`.`mybatis_plus`(`id`, `name`, `age`) VALUES (#{id}, #{name}, #{age});")
    void insertTest(MybatisPlus mybatisPlus);
}
