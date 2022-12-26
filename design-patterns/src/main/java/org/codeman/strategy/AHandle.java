package org.codeman.strategy;

import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/26
 */
@Component
public class AHandle implements FileHandleInterface {

    @Override
    public String getType() {
        return FileHandleTypeEnum.A_TYPE.toString();
    }

    @Override
    public String handleFile(String parameter) {
        return FileHandleTypeEnum.A_TYPE.toString() + " " + parameter;
    }

}
