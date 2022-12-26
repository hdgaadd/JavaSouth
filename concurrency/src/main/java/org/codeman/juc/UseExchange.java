package org.codeman.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Exchanger;

/**
 * @author hdgaadd
 * created on 2022/12/26
 *
 * description: 用于两个线程之间的数据交换，当数据交换成功后才可进行执行后面逻辑。线程数不能超过2个，该类实际应用场景不多
 */
@Slf4j
public class UseExchange {

    private static final Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                String msg = "msg0";
                String getMsg = exchanger.exchange(msg);
                log.info(String.format("%s got msg is %s", Thread.currentThread().getName(), getMsg));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                String msg = "msg1";
                String getMsg = exchanger.exchange(msg);
                log.info(String.format("%s got msg is %s", Thread.currentThread().getName(), getMsg));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
