package com.codeman.util;

import java.util.concurrent.*;

/**
 * @author hdgaadd
 * created on 2022/01/31
 */
public class ThreadPoolUtil {
    /**
     * 缓存的线程数
     */
    private static final int SIZE_CORE_POOL = 5;
    /**
     * 线程上限数
     */
    private static final int SIZE_MAX_POOL =10;
    /**"
     * 线程存活时间
     */
    private static final long ACTIVE_TIME = 2000;
    /**
     * 线程池阻塞队列类型
     */
    private static final BlockingQueue<Runnable> BQUEUE = new ArrayBlockingQueue<>(100);
    /**
     * 自定义线程池
     */
    private static final ExecutorService POOL = new ThreadPoolExecutor(SIZE_CORE_POOL, SIZE_MAX_POOL, ACTIVE_TIME, TimeUnit.MILLISECONDS, BQUEUE, new ThreadPoolExecutor.CallerRunsPolicy());

    public static ExecutorService getPool() {
        return POOL;
    }
}
