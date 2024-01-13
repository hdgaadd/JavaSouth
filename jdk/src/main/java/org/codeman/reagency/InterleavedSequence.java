package org.codeman.reagency;

import java.util.Scanner;

/**
 * @author hdgaadd
 * created on 2023/12/27
 *
 * reference: <a href="https://www.nowcoder.com/practice/d00c43a0739e4f0ca299d6c5067bb4b9?tpId=182&tqId=34422&rp=1&ru=/exam/oj&qru=/exam/oj&sourceUrl=%2Fexam%2Foj%3Fdifficulty%3D2%26page%3D1%26pageSize%3D50%26search%3D%26tab%3D%25E5%2590%258D%25E4%25BC%2581%25E7%259C%259F%25E9%25A2%2598%26topicId%3D182&difficulty=2&judgeStatus=undefined&tags=&title=">...</a>
 */
public class InterleavedSequence {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        int ret = 1;

        String line = sc.nextLine(), cur = "";
        for (String l : line.split(" ")) {
            if (cur.equals("")) {
                cur = l;
            } else {
                if (!l.equals(cur)) {
                    ret++;
                    cur = l;
                }
            }
        }
        System.out.println(ret);
    }
}
