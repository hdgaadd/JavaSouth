package org.codeman.build.component;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hdgaadd
 * Created on 2022/10/04
 */
@Data
@AllArgsConstructor
public class Server {

    private String system;

    @Override
    public String toString() {
        return "the server operating system is " + system;
    }
}
