package org.codeman.readwritelock;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author hdgaadd
 * created on 2022/12/25
 *
 * description: 相对于普通的对象锁解决读写问题，读写锁适用于读写在两个不同的方法中的场景
 */
@Slf4j
public class UseReadWriteLock {

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

        private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        public void put(Integer index, String val) {
            readWriteLock.writeLock().lock();

            try {
                Thread.sleep(1000);

                CACHE_MAP.put(index, val);
                log.info(String.format("%s put val is %s", Thread.currentThread().getName(), index));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }

        public void get(Integer index) {
            readWriteLock.readLock().lock();

            try {
                Thread.sleep(1000);

                log.info(String.format("%s get val is %s", Thread.currentThread().getName(), CACHE_MAP.get(index)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readWriteLock.readLock().unlock();
            }
        }
    }
}
