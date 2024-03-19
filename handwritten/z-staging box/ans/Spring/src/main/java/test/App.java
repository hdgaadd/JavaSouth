package test;

import org.codeman.context.ClassPathXmlApplicationContext;
import org.codeman.context.ApplicationContext;
import test.service.UserService;

/**
 * @author hdgaadd
 * created on 2022/05/06
 */
public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");

        UserService userService = (UserService) ctx.getBean("userService");
        System.out.println(userService.getUser());
        // NullPointException原因为：核心类没有开发完毕
        // TODO: 完善相关核心类
    }
}
