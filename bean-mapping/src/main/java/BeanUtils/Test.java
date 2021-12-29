package BeanUtils;

import mapstruct.Convert;
import org.springframework.beans.BeanUtils;

/**
 * @author hdgaadd
 * Created on 2021/12/27
 */
public class Test {
    public static void main(String[] args) {
        A a = new A();
        a.setVal(1);
        B b = new B();
        BeanUtils.copyProperties(a, b);
        System.out.println(b);
    }
}
