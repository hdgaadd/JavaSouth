package mapstruct;

/**
 * @author hdgaadd
 * Created on 2021/12/27
 */
public class Test {
    public static void main(String[] args) {
        A a = new A();
        a.setVal(1);
        B b = Convert.INSTANCE.aToB(a);
        System.out.println(b);
    }
}
