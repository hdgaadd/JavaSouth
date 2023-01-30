package org.codeman.jvm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hdgaadd
 * created on 2023/01/25
 */
/*
reference:
- https://blog.csdn.net/yuanshangshenghuo/article/details/108394472
- https://blog.csdn.net/health7788/article/details/123893540

C:\Java\jdk1.8.0_311\bin>jmap -dump:live,format=b,file=dmp.hprof 11628
Dumping heap to C:\Java\jdk1.8.0_311\bin\dmp.hprof ...
Heap dump file created

 */
public class Jmap {

    private static final List<Data> list = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 200; i++) {
            list.add(new Data());
        }

        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Data {
        private final byte[] arr = new byte[1024 * 1024];
    }
}
