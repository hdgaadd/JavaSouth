package org.codeman.juc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author hdgaadd
 * created on 2022/12/26
 *
 * descirption: 相对于CountDownLatch、CyclicBarrier，Phaser可动态注册需要协调的线程，规定线程的执行屏障
 */
@Slf4j
public class UsePhaser {

    private static final Phaser phaser = new Phaser();

    public static void main(String[] args) {
        for (int i = 0; i < 6; i++) {
            new Task(phaser).start();
        }

        phaser.register();
        phaser.arriveAndAwaitAdvance();
        log.info("all works finished!");
    }

    private static class Task extends Thread {

        private final Phaser phaser;

        public Task(Phaser phaser) {
            this.phaser = phaser;
            this.phaser.register();
        }

        @SneakyThrows
        @Override
        public void run() {
            TimeUnit.SECONDS.sleep(new Random().nextInt(5));
            log.info(String.format("%s work finished", getName()));
            this.phaser.arriveAndAwaitAdvance();
        }
    }

}
