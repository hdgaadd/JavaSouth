package com.codeman.stringJoiner;

import java.util.StringJoiner;

/**
 * @author hdgaadd
 * Created on 2022/03/18
 */
public class Detail {
    public static void main(String[] args) {
        StringJoiner stringJoiner = new StringJoiner(", ", "输出：", "."); // [ˈsʌfɪks]后缀 [dɪˈlɪmɪtə(r)]分隔符
        stringJoiner.add("hdgaadd")
                .add("codeman");

        System.out.println(stringJoiner);
    }
}
