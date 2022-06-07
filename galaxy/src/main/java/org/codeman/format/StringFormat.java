package org.codeman.format;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * Created on 2022/05/29
 */
@Slf4j
public class StringFormat {
    private static final String FORMAT = "hdgaadd_%s";

    private static final String testData = "data";

    public static void main(String[] args) {
        // 使用指定的格式化，将数据进行格式化
        stringFormat(testData);
    }

    private static void stringFormat(String data) {
        log.info(String.format(FORMAT, testData));
    }
}
