package org.codeman.executor.parameter;

import java.sql.PreparedStatement;

/**
 * @author hdgaadd
 * created on 2022/02/28
 *
 */
public class DefaultParameterHandler implements ParameterHandler {

    private Object parameter;

    public DefaultParameterHandler(Object parameter) {
        this.parameter = parameter;
    }

    /**
     * 把PreparedStatement转换为数组类型
     */
    public void setParameters(PreparedStatement preparedStatement) {
        try {
            if (parameter != null) {
                if (parameter.getClass().isArray()) {
                    Object[] parameterArr = (Object[]) parameter;
                    for (int i = 0; i < parameterArr.length; i++) {
                        preparedStatement.setObject(i + 1, parameterArr[i]); // parameter的长度应该比preparedStatement多一位
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
