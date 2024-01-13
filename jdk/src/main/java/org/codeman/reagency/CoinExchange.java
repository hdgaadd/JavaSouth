package org.codeman.reagency;

import java.util.Scanner;

/**
 * @author hdgaadd
 * created on 2024/01/06
 *
 * reference: <a href="https://www.nowcoder.com/practice/4f900b1c941c45288dba06baa006907f?tpId=182&tqId=34426&rp=1&ru=/exam/oj&qru=/exam/oj&sourceUrl=%2Fexam%2Foj%3Fdifficulty%3D2%26page%3D1%26pageSize%3D50%26search%3D%26tab%3D%25E5%2590%258D%25E4%25BC%2581%25E7%259C%259F%25E9%25A2%2598%26topicId%3D182&difficulty=2&judgeStatus=undefined&tags=&title=">...</a>
 */
public class CoinExchange {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int money = sc.nextInt();
        int[] arr = {1, 2, 5, 10, 20, 50, 100};

        int cCount = 0, nCount = 0;
        for (int a : arr) {
            if (money - a >= 0) {
                cCount++;
                money -= a;
            } else {
                break;
            }
        }
        nCount = cCount + money;
        System.out.println(cCount + " " + nCount);
    }
}
