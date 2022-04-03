package com.codeman;

import lombok.Data;

/**
 * @author hdgaadd
 * Created on 2022/03/30
 */
@Data
public abstract class YearBase {
    /**
     * 当前年份
     */
    public String year;
    /**
     * 一月份所在水库周期（汛期或旱期）
     */
    public String cycleInJanuary;
    /**
     * 二月份所在水库周期（汛期或旱期）
     */
    public String cycleInFebruary;
    /**
     * 三月份所在水库周期（汛期或旱期）
     */
    public String cycleInMarch;
    /**
     * 四月份所在水库周期（汛期或旱期）
     */
    public String cycleInApril;
    /**
     * 五月份所在水库周期（汛期或旱期）
     */
    public String cycleInMay;
    /**
     * 六月份所在水库周期（汛期或旱期）
     */
    public String cycleInJune;
    /**
     * 七月份所在水库周期（汛期或旱期）
     */
    public String cycleInJuly;
    /**
     * 八月份所在水库周期（汛期或旱期）
     */
    public String cycleInAugust;
    /**
     * 九月份所在水库周期（汛期或旱期）
     */
    public String cycleInSeptember;
    /**
     * 十月份所在水库周期（汛期或旱期）
     */
    public String cycleInOctober;
    /**
     * 十一月份所在水库周期（汛期或旱期）
     */
    public String cycleInNovember;
    /**
     * 十二月份所在水库周期（汛期或旱期）
     */
    public String cycleInDecember;

}
