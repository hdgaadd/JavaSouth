package com.codeman;

/**
 * @author hdgaadd
 * Created on 2022/03/30
 */
public enum CycleEnum {

    FLOOD_SEASON("汛期"),
    DRY_SEASON("旱期"),
    NO_DATA("暂时没定义该月份");
    private final String cycleName;

    CycleEnum(String cycleName) {
        this.cycleName = cycleName;
    }

    public String getCycleName() {
        return this.cycleName;
    }
}
