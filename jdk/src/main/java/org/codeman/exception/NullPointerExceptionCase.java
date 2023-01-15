package org.codeman.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2023/01/07
 */
@Slf4j
public class NullPointerExceptionCase {

    private static final Integer NULL_VAR = null;

    private static final Component NULL_ENTITY = null;

    private static final Component NULL_ENTITY_VAL = new Component();

    public static void main(String[] args) {
        // null进行==
        case0();

        // null进行equals
        // 不会报错情况："".equals(null);
        case1();

        // null进行get属性
        case2();

        // null进行toString
        case3();
    }

    private static void case0() {
        try {
            if (NULL_VAR == 1);
        } catch (NullPointerException e) {
            log.error("case0: this is an error!");
        }
    }

    private static void case1() {
        try {
            if (NULL_VAR.equals("1"));
        } catch (NullPointerException e) {
            log.error("case1: this is an error!");
        }
    }

    private static void case2() {
        try {
            NULL_ENTITY.getName();
        } catch (NullPointerException e) {
            log.error("case2: this is an error!");
        }
    }

    private static void case3() {
        try {
            NULL_ENTITY_VAL.getName().toString();
        } catch (NullPointerException e) {
            log.error("case3: this is an error!");
        }
    }

    @Data
    private static class Component {
        String name;
    }
}
