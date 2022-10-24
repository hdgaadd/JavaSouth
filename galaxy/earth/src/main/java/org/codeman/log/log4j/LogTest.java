package org.codeman.log.log4j;

import org.apache.log4j.Logger;

/**
 * @author hdgaadd
 * created on 2022/01/22
 */
public class LogTest { // 需要配置log4j.properties
    private static final Logger logger = Logger.getLogger(LogTest.class);

    public static void main(String[] args) {
        logger.info("info");
        logger.debug("Here have debug");
        logger.warn("warning");
        logger.error("error");
    }
}
