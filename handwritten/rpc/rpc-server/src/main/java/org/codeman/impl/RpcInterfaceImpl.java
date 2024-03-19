package org.codeman.impl;

import org.codeman.Hello;
import org.codeman.RpcInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcInterfaceImpl implements RpcInterface {

    private static final Logger logger = LoggerFactory.getLogger(RpcInterfaceImpl.class);

    public String hello(Hello hello) {
        logger.info("server收到: {}.", hello.toString());
        String result = "Hello's msg is " + hello.getMessage() + ", Hello'S description is " + hello.getDescription();
        logger.info("server返回: {}.", result);
        return result;
    }

}
