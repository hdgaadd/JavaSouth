package org.codeman.chain;


import org.codeman.chain.objs.Boss;
import org.codeman.chain.objs.Employee;
import org.codeman.chain.objs.GroupManager;

/**
 * @author hdgaadd
 * Created on 2022/04/19
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
