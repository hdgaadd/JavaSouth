package threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NoSecurity {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            int number = i;
            es.execute(() -> System.out.println(number + ":" + new intUtil().addTen(number)));
        }
    }
    static class intUtil {
        public static int num = 0; // 关键点在于num是共享变量，下一个线程修改该值，上一个线程的获取该值也会改变

        public int addTen(int number) { // num起到保存number的作用
            num = number;

            try { // 休息1秒，让下一个线程可以提前运行
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return num + 10;
        }
    }
}


