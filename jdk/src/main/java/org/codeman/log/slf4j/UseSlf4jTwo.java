package org.codeman.log.slf4j;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/01/27
 */
@Slf4j
public class UseSlf4jTwo { // 只需引入lombok即可

    public static void main(String[] args) {
        log.info("info");
        log.debug("debug");
        log.warn("warn");
        log.error("error");
    }
}
