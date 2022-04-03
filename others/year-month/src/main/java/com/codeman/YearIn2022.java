package com.codeman;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hdgaadd
 * Created on 2022/03/30
 */
public class YearIn2022 extends YearBase {

    // 当省领导主持会议，决定汛期时间时，给以下变量赋值
    public YearIn2022() {
        super.year = "2022";
        super.cycleInJanuary = CycleEnum.FLOOD_SEASON.getCycleName(); // 设置一月份为汛期
        super.cycleInFebruary = CycleEnum.DRY_SEASON.getCycleName(); // 设置二月份为旱期

        // 以下未定义
        super.cycleInMarch = CycleEnum.NO_DATA.getCycleName();
        super.cycleInApril = CycleEnum.NO_DATA.getCycleName();
        super.cycleInMay = CycleEnum.NO_DATA.getCycleName();
        super.cycleInJune = CycleEnum.NO_DATA.getCycleName();
        super.cycleInJuly = CycleEnum.NO_DATA.getCycleName();
        super.cycleInAugust = CycleEnum.NO_DATA.getCycleName();
        super.cycleInSeptember = CycleEnum.NO_DATA.getCycleName();
        super.cycleInOctober = CycleEnum.NO_DATA.getCycleName();
        super.cycleInNovember = CycleEnum.NO_DATA.getCycleName();
        super.cycleInDecember = CycleEnum.NO_DATA.getCycleName();
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String datetime = "2022010202";
        System.out.println(getCycleName(datetime));
    }

    private static String getCycleName(String datetime) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        /**
         * 当前年份
         */
        String thisYear = datetime.substring(0, 4);
        /**
         * 当前月份
         */
        String thisMonth = datetime.substring(4, 6);
        /**
         * 某月份对应的方法名Map
         */
        Map<String, String> methNameMap = new HashMap<String, String>(){{
            put("01", "CycleInJanuary");
        }};

        /**
         * 某月份对应的方法名
         */
        String getCycleName = "get" + methNameMap.get(thisMonth);

        // 获取当前时间的水库周期（汛期或旱期）
        Class clazz = Class.forName("com.codeman.YearIn" + thisYear);
        Object o = clazz.newInstance();
        Method method = clazz.getMethod(getCycleName);

        return (String) method.invoke(o);
    }
}
