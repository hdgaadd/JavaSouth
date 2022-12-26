package org.codeman.log.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hdgaadd
 * created on 2022/01/27
 */
public class UseSlf4j0 { // 只需引入lombok即可
    private static final Logger logger = LoggerFactory.getLogger(UseSlf4j0.class);

    public static void main(String[] args) {
        logger.info("info");
        logger.debug("debug");
        logger.warn("warn");
        logger.error("error");
    }
}
