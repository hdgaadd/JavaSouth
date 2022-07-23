package org.codeman.constant;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author hdgaadd
 * Created on 2021/12/13
 */
@Mapper
public interface CommonConstant {
    /**
     * 成功状态码
     */
    Integer SUCCESS = 1;

    /**
     * 失败状态码
     */
    Integer FAIL = 0;
}
