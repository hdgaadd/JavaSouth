package org.codeman.string;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/05/29
 */
@Slf4j
public class StringFormat {

    private static final String FORMAT = "hdgaadd_%s";

    private static final String testData = "data";

    public static void main(String[] args) {
        // 使用指定的格式化，将数据进行格式化
        stringFormat();

        // 模糊查询
        fuzzyQuery();
    }

    private static void stringFormat() {
        log.info(String.format(FORMAT, testData));
    }

    private static void fuzzyQuery() {
        log.info(String.format("like '%%%s%%'", "name"));
    }
}
