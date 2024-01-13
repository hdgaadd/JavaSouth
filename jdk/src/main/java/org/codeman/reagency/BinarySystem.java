package org.codeman.reagency;

/**
 * @author hdgaadd
 * created on 2023/12/23
 *
 * reference: <a href="https://www.nowcoder.com/practice/120e406db3fd46f09d55d59093f13dd8?tpId=182&tqId=34357&rp=1&ru=/exam/oj&qru=/exam/oj&sourceUrl=%2Fexam%2Foj%3Fdifficulty%3D2%26page%3D1%26pageSize%3D50%26search%3D%26tab%3D%25E5%2590%258D%25E4%25BC%2581%25E7%259C%259F%25E9%25A2%2598%26topicId%3D182&difficulty=2&judgeStatus=undefined&tags=&title=">...</a>
 */
public class BinarySystem {
    public int countBitDiff (int m, int n) {
        int ret = 0;
        int ans = m ^ n; // 异或得到不同的位数，标记为1
        while (ans != 0) {
            ret += ans & 1; // 计算1的个数
            ans = ans >> 1;
        }
        return ret;
    }
}
