package org.codeman.chain;


/**
 * @author hdgaadd
 * created on 2022/04/19
 *
 * process
 * - 责任链模式就像踢皮球，遇到无法处理的困难，抛给上级去处理
 */
public class Client {
    public static void main(String[] args) {
        GroupManager groupManager = new GroupManager();
        Boss boss = new Boss();
        groupManager.setNextHandler(boss); // 组长的上级是boss

        Employee e1 = new Employee("员工1", 1);
        Employee e2 = new Employee("员工2", 3);
        Employee e3 = new Employee("员工3", 6);

        groupManager.doHandler(e1);
        groupManager.doHandler(e2);
        groupManager.doHandler(e3);
    }
}
