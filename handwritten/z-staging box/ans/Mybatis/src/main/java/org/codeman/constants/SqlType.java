package org.codeman.constants;

/**
 * @author hdgaadd
 * created on 2022/02/23
 * description: SQL操作类型
 */
public enum SqlType {
    SELECT("select"),
    INSERT("insert"),
    UPDATE("update"),
    DEFAULT("default");

    private String value;

    SqlType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
