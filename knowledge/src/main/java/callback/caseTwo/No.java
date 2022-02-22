package callback.caseTwo;

import callback.caseTwo.component.A;
import callback.caseTwo.component.B;
import callback.caseTwo.component.Resource;

import java.util.List;

/**
 * @author hdgaadd
 * Created on 2022/02/06
 */
public class No {
    public static void main(String[] args){
        Resource resource = new Resource();

        List<String> isA = handle(resource.getIsA()); // need two
        new A().same(isA);  // need two
        List<String> isB = handle(resource.getIsB());
        new B().same(isB);
    }

    private static List<String> handle(List<String> resource) {
        // 处理属于不同实例的List资源
        for (int i = 0; i < resource.size(); i++) {
            resource.set(i, resource.get(i) + i);
        }
        return resource;
    }
}
