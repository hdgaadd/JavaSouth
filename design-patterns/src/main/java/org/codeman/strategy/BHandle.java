package org.codeman.strategy;

import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/26
 */
@Component
public class BHandle implements FileHandleInterface {

    @Override
    public String getType() {
        return FileHandleTypeEnum.B_TYPE.toString();
    }

    @Override
    public String handleFile(String parameter) {
        return FileHandleTypeEnum.B_TYPE.toString() + " " + parameter;
    }

}
