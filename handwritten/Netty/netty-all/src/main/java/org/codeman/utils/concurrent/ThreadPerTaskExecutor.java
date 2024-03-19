package org.codeman.utils.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * @author hdgaadd
 * created on 2022/04/11
 */
public class ThreadPerTaskExecutor implements Executor {
    /**
     * DefaultThreadFactory
     */
    private final ThreadFactory threadFactory;

    public ThreadPerTaskExecutor(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    @Override
    public void execute(Runnable command) {
        //每次创建一个线程 DefaultThreadFactory.newThread(java.lang.Runnable)
        threadFactory.newThread(command).start();
    }
}
