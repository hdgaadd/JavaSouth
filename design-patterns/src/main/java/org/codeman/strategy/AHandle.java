package org.codeman.strategy;

import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/26
 */
@Component
public class AHandle implements HandleInterface {

    @Override
    public String getType() {
        return HandleTypeEnum.A_TYPE.toString();
    }

    @Override
    public String handle(String parameter) {
        return HandleTypeEnum.A_TYPE.toString() + " " + parameter;
    }

}
