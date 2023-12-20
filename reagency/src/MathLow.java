import java.util.Scanner;

/**
 * @author hdgaadd
 * created on 2023/12/20
 *
 * description: 数学规律：2n -3
 * reference: <a href="https://www.nowcoder.com/practice/f96f4b55c1c44636a41d1eb2b04ee202?tpId=182&tqId=34431&rp=1&ru=/exam/oj&qru=/exam/oj&sourceUrl=%2Fexam%2Foj%3Fdifficulty%3D2%26page%3D1%26pageSize%3D50%26search%3D%26tab%3D%25E5%2590%258D%25E4%25BC%2581%25E7%259C%259F%25E9%25A2%2598%26topicId%3D182&difficulty=2&judgeStatus=undefined&tags=&title=">...</a>
 */
class MathLow {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextInt()) {
            System.out.println(2 * sc.nextInt() - 3);
        }
    }
}