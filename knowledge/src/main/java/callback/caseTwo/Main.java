package callback.caseTwo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hdgaadd
 * Created on 2022/01/02
 */
public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // 通过回调函数，调用n个类的相同方法
        new Main().global(new One());
        new Main().global(new Two());
    }

    public void global(Object o) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Method method = o.getClass().getMethod("sout");
        method.invoke(o.getClass().newInstance());
    }
}
