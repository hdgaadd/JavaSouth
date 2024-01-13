package org.codeman.reagency;

/**
 * @author hdgaadd
 * created on 2024/01/05
 *
 * reference: <a href="https://leetcode.cn/problems/integer-replacement/">...</a>
 */
public class IntegerReplacement {
    public int integerReplacement(int n) {
        if (n <= 1) return 0;
        if (n % 2 == 0) return 1 + integerReplacement(n / 2);

        return 2 + Math.min(integerReplacement((n + 1) / 2), integerReplacement((n - 1) / 2));
    }
}
