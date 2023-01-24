package org.codeman.chain;


/**
 * @author hdgaadd
 * created on 2022/04/19
 *`
 * description
 * - 相比于调用方法的实现，责任链避免了违背开闭原则，把具体逻辑解耦、把移动方法来调整执行修改为调整调用链
 */
public class Client {
    public static void main(String[] args) {
        // 优惠券领取校验
        NewUserCheck checker = new NewUserCheck();
        checker.setNextChecker(new CompleteShareCheck())
                .setNextChecker(new ExceededLimitCheck());

        checker.check(new User(false, true, true));
        System.out.println();

        checker.check(new User(true, false, false));
        System.out.println();

        checker.check(new User(true, true, false));
        System.out.println();

        checker.check(new User(true, true, true));
    }
}
