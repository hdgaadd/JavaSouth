package org.codeman.build.component;

import org.codeman.build.builder.Builder;

/**
 * @author hdgaadd
 * Created on 2022/10/04
 *
 * description: 供内部调用，客户端的内部调用器
 */
public class ServerDirector {

    public <T> T construct(Builder<T> builder) {
        builder.setServer();
        return builder.getServer();
    }
}
