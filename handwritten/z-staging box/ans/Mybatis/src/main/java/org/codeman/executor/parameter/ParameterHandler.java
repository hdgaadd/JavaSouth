package org.codeman.executor.parameter;

import java.sql.PreparedStatement;

/**
 * @author hdgaadd
 * created on 2022/02/28
 * description: [pəˈræmɪtə(r)] 底层规范
 */
public interface ParameterHandler {
    /**
     * 设置参数
     */
    void setParameters(PreparedStatement preparedStatement);
}
