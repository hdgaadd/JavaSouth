package org.codeman.Queue.BlockingQueue;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyArrayBlockingQueue<E> {

    final Object[] items;

    int count; // 当前数组个数

    int putIndex;

    int takeIndex;

    final ReentrantLock lock;

    private final Condition notFull;

    private final Condition notEmpty;

    public MyArrayBlockingQueue(int capacity) {
        this(capacity, false);
    }

    public MyArrayBlockingQueue(int capacity, boolean fair) {
        items = new Object[capacity];
        lock = new ReentrantLock(fair); // 是否实现公平锁
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
    }

    public void put(E element) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (count == items.length)
                notFull.await(); // 标记该线程为变量名为notFull的Condition
            enqueue(element); // 添加元素
        } finally {
            lock.unlock();
        }
    }

    public void enqueue(E element) {
        items[putIndex] = element;
        if (++putIndex == items.length) {
            putIndex = 0; // 由于是FIFO,当添加元素到length-1时，把下一个添加元素的下标置为0
        }
        count++;
        notEmpty.signal();
    }

    public E take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (count == 0)
                notEmpty.await();
            return dequeue(); // 添加元素
        } finally {
            lock.unlock();
        }
    }

    public E dequeue() {
        final Object[] items = this.items;
        E element = (E) items[takeIndex];
        items[takeIndex] = null;
        if (++takeIndex == items.length) {
            takeIndex = 0; // 由于是FIFO,当删除元素到length-1时，把下一个添加元素的下标置为0
        }
        count--;
        notFull.signal();
        return element;
    }

    public static void main(String[] args) throws InterruptedException {
        MyArrayBlockingQueue<Integer> integerMyArrayBlockingQueue = new MyArrayBlockingQueue<Integer>(2);
        integerMyArrayBlockingQueue.put(1);
        integerMyArrayBlockingQueue.put(2);
        System.out.println(integerMyArrayBlockingQueue.take());
        integerMyArrayBlockingQueue.put(3);
        System.out.println(Arrays.toString(integerMyArrayBlockingQueue.items));
    }

}
