package com.codeman.log.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hdgaadd
 * Created on 2022/01/27
 */
public class LogOne { // 只需引入lombok即可
    private static final Logger logger = LoggerFactory.getLogger(LogOne.class);

    public static void main(String[] args) {
        logger.info("info");
        logger.debug("debug");
        logger.warn("warn");
        logger.error("error");
    }
}
