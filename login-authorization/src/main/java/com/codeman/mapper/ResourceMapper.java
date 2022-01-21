package com.codeman.mapper;

import com.codeman.domain.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hdgaadd
 * @since 2021-11-28
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> getResourceList(@Param("adminId") Long adminId);
}
