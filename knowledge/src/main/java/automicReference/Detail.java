package automicReference;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hdgaadd
 * Created on 2022/03/18
 */
public class Detail {
    public static void main(String[] args) {
        AtomicReference<Double> ar = new AtomicReference<>(1D);
        ar.updateAndGet(v -> v + 6D);

        System.out.println(ar);
    }
}
