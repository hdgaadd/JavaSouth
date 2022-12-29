package org.codeman.strategy;

import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/26
 */
@Component
public class BHandle implements HandleInterface {

    @Override
    public String getType() {
        return HandleTypeEnum.B_TYPE.toString();
    }

    @Override
    public String handleFile(String parameter) {
        return HandleTypeEnum.B_TYPE.toString() + " " + parameter;
    }

}
