package org.codeman;

import org.codeman.component.A;
import org.codeman.component.B;
import org.codeman.component.Resource;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hdgaadd
 * Created on 2022/01/02
 */
public class Main {
    // 通过回调函数，使用一个方法就可：调用n个类的相同方法，且顺便处理每一个属于不同实例的List资源
    // 不通过回调函数，需使用两个方法：1.大量重复书写相同方法名，2.创建另一个方法来单独处理每一个List资源
    public static void main(String[] args){
        Resource resource = new Resource();

        callback(new A(), resource.getIsA()); // just need one
        callback(new B(), resource.getIsB());
    }

    private static void callback(Object o, List<String> resource){
        try {
            // 处理属于不同实例的List资源
            for (int i = 0; i < resource.size(); i++) {
                resource.set(i, resource.get(i) + i);
            }

            Method method = o.getClass().getMethod("same", List.class);
            method.invoke(o, resource);
        } catch (Exception e) {
            System.out.println("error");
        }
    }
}
