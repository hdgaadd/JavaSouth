import java.util.Scanner;

/**
 * @author hdgaadd
 * created on 2023/12/21
 *
 * reference: <a href="https://www.nowcoder.com/practice/45083499b8c5404fb1db44c6ea4f170a?tpId=182&tqId=34330&rp=1&ru=/exam/oj&qru=/exam/oj&sourceUrl=%2Fexam%2Foj%3Fdifficulty%3D2%26page%3D1%26pageSize%3D50%26search%3D%26tab%3D%25E5%2590%258D%25E4%25BC%2581%25E7%259C%259F%25E9%25A2%2598%26topicId%3D182&difficulty=2&judgeStatus=undefined&tags=&title=">...</a>
 */
public class Abbreviation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String curStr = sc.nextLine();
            String[] curArr = curStr.split(" ");
            String retStr = "";
            for (String str : curArr) {
                retStr += str.charAt(0);
            }
            System.out.println(retStr);
        }
    }
}

