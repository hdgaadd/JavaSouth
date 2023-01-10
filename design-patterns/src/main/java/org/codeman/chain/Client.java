package org.codeman.chain;


/**
 * @author hdgaadd
 * created on 2022/04/19
 *
 * description
 * - 责任链模式就像踢皮球，遇到无法处理的困难，抛给上级去处理
 * - 相比于调用方法的实现，责任链把具体逻辑解耦、把移动方法来调整执行修改为设置变量
 */
public class Client {
    public static void main(String[] args) {
        GroupManagerHandler groupManager = new GroupManagerHandler();
        groupManager.setNextHandler(new BossHandler())
                    .setNextHandler(new SuperBossHandler());

        groupManager.handler(new Employee("员工1", 1));
        groupManager.handler(new Employee("员工2", 3));
        groupManager.handler(new Employee("员工3", 5));
        groupManager.handler(new Employee("员工4", 6));
    }
}
