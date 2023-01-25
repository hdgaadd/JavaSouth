package org.codeman.business;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * @author hdgaadd
 * created on 2023/01/25
 *
 * description: 使用信号量来设置本地缓存容量，防止缓存添加过多造成的内存溢出
 *
 * querstion: 容量满了，添加新容量的线程会一直阻塞，阻塞线程过多会消耗大量资源；当容量空闲了，阻塞的线程会添加缓存，但此时缓存时间已到期，再添加缓存没有意义
 */
public class SemaphoreCache<K, V> {

    private final int LIMIT_SIZE;

    private final Semaphore semaphore;

    public final Map<K, V> CACHE_MAP;

    public SemaphoreCache(int limitSize) {
        this.LIMIT_SIZE = limitSize;
        this.semaphore = new Semaphore(limitSize);
        CACHE_MAP = new ConcurrentHashMap<K, V>(LIMIT_SIZE);
    }

    public V put(K k, V v) {
        boolean wasAdd = false;
        try {
            semaphore.acquire();

            V val = CACHE_MAP.putIfAbsent(k, v);
            // 不为空说明已有该key
            if (val != null) {
                return val;
            }
            wasAdd = true;
        } catch (Exception e) {

        } finally {
            if (!wasAdd) {
                semaphore.release();
            }
        }
        return v;
    }

    public V get(K k) {
        return CACHE_MAP.get(k);
    }

    public void removeFirst() {
        K firstK = null;
        for (K key : CACHE_MAP.keySet()) {
            firstK = key;
            break;
        }

        V removeV = CACHE_MAP.remove(firstK);
        if (removeV != null) {
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SemaphoreCache<String, String> cache = new SemaphoreCache<String, String>(3);

        Runnable runnable = () -> {
            int num = new Random().nextInt(1000);
            cache.put(num + "", num + "");
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
        Thread.sleep(1000);
        System.out.println(cache.CACHE_MAP);

        new Thread(runnable).start();
        Thread.sleep(1000);
        System.out.println(cache.CACHE_MAP);
        Thread.sleep(1000);

        cache.removeFirst();
        System.out.println(cache.CACHE_MAP);

        Thread.sleep(1000);
        System.out.println(cache.CACHE_MAP);
    }

}
