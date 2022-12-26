package org.codeman.readwritelock;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hdgaadd
 * created on 2022/12/25
 */
@Slf4j
public class UnUseReadWriteLock {

    private static final Cache cache = new Cache();

    public static void main(String[] args) {
        for (int i = 0; i < 6; i++) {
            final int index = i;
            new Thread(() -> cache.put(index, String.valueOf(index))).start();
        }

        for (int i = 0; i < 6; i++) {
            final int index = i;
            new Thread(() -> cache.get(index)).start();
        }
    }

    private static class Cache {

        private static final Map<Integer, String> CACHE_MAP = new HashMap<>();

        public void put(Integer index, String val) {
            try {
                Thread.sleep(1000);

                CACHE_MAP.put(index, val);
                log.info(String.format("%s put val is %s", Thread.currentThread().getName(), index));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void get(Integer index) {
            try {
                Thread.sleep(1000);

                log.info(String.format("%s get val is %s", Thread.currentThread().getName(), CACHE_MAP.get(index)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
