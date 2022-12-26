package org.codeman.callback.case1;

import org.codeman.callback.case1.component.*;

import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/02/06
 */
public class UnUseCallBack {

    public static void main(String[] args){
        Resource resource = new Resource();

        List<String> isA = handle(resource.getIsA());
        new A().same(isA);  // need same()
        List<String> isB = handle(resource.getIsB());
        new B().same(isB); // need same()
        List<String> isC = handle(resource.getIsC());
        new C().same(isC); // need same()
        List<String> isD = handle(resource.getIsD());
        new D().same(isD); // need same()
        List<String> isE = handle(resource.getIsE());
        new E().same(isE); // need same()
    }

    private static List<String> handle(List<String> resource) {
        // 处理属于不同实例的List资源
        for (int i = 0; i < resource.size(); i++) {
            resource.set(i, resource.get(i) + i);
        }
        return resource;
    }
}
