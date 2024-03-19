package org.codeman.context;

/**
 * @author hdgaadd
 * created on 2022/03/03
 * description: 继承规范实现者，添加其他功能
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    public ClassPathXmlApplicationContext(String fileName) {
        super.configFile = fileName;
        super.refresh();
    }
}
