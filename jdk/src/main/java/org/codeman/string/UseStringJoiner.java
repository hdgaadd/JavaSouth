package org.codeman.string;

import java.util.StringJoiner;

/**
 * @author hdgaadd
 * created on 2022/03/18
 *
 * descirption: 创造一个字符序列，可添加前缀、后缀、分隔符
 */
public class UseStringJoiner {
    public static void main(String[] args) {
        StringJoiner stringJoiner = new StringJoiner(", ", "输出：", "."); // [ˈsʌfɪks]后缀 [dɪˈlɪmɪtə(r)]分隔符
        stringJoiner.add("hdgaadd")
                .add("codeman");

        System.out.println(stringJoiner);
    }
}
