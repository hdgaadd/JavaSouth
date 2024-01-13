package org.codeman.reagency;

import java.util.Scanner;

/**
 * @author hdgaadd
 * created on 2023/12/29
 *
 * reference: <a href="https://www.nowcoder.com/practice/967133d6656440ba951870eaf17861de?tpId=182&tqId=34453&rp=1&ru=/exam/oj&qru=/exam/oj&sourceUrl=%2Fexam%2Foj%3Fdifficulty%3D2%26page%3D1%26pageSize%3D50%26search%3D%26tab%3D%25E5%2590%258D%25E4%25BC%2581%25E7%259C%259F%25E9%25A2%2598%26topicId%3D182&difficulty=2&judgeStatus=undefined&tags=&title=">...</a>
 */
public class TheNthValue {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int i = 0;
        while(n > 0) {
            i++;
            n = n - i;
        }
        System.out.println(i);
    }
}
