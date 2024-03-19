package org.codeman.Queue.PriorityQueue;

import java.util.Comparator;

public class MyPriorityQueue<E> {

    static int DEFAULT_INITIAL_CAPACITY = 11;

    Comparator<? super E> comparator;

    Object[] queue;

    int size = 0; // 数组当前元素个数

    public MyPriorityQueue() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    public MyPriorityQueue(Comparator<? super E> comparator) { // 第一种实例PriorityQueue方式
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    public MyPriorityQueue(int capacity, Comparator<? super E> comparator) { // 第二种实例PriorityQueue方式
        this.comparator = comparator;
        queue = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void offer(E element) {
        int i = size;
        size++;
        if (size == 0) {
            queue[0] = element;
        } else {
            siftUp(i, element);
        }
    }

    public void siftUp(int index, E element) {
        if (comparator != null)
            siftUpUsingComparator(index, element);
        else
            siftUpComparable(index, element);
    }

    public void siftUpComparable(int index, E element) { // index为新元素的下标
        Comparable<? super E> e = (Comparable<? super E>) element;
        while (index > 0) { // 循环，直到要添加的数据放到某位置，使数组还是从大到小排序
            int indexPrev = (index - 1) >>> 1; // 原数组第一个数下标
            E prev = (E) queue[indexPrev];
            if (e.compareTo(prev) >= 0)
                break; // 要添加的数据大于原数组第一个数，直接跳出循环，在最后面添加
            queue[index] = prev; // 要添加的数据小于原数组所有数第一个数，交互位置
            index = indexPrev; // 下标后移
        }
        queue[index] = element;
    }

    public void siftUpUsingComparator(int index, E element) { // 覆盖Comparator添加数据
        while (index > 0) { // 循环，直到要添加的数据放到某位置，使数组还是从大到小排序
            int indexPrev = (index - 1) >>> 1; // 原数组第一个数下标
            E prev = (E) queue[indexPrev];
            if (comparator.compare(element, prev) >= 0)
                break; // 要添加的数据大于原数组第一个数，直接跳出循环，在最后面添加
            queue[index] = prev; // 要添加的数据小于原数组所有数第一个数，交互位置
            index = indexPrev; // 下标后移
        }
        queue[index] = element;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(queue[i]);
            sb.append(",");
        }
        sb = sb.delete(sb.length() - 1, sb.length());
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        MyPriorityQueue<Integer> priorityQueue = new MyPriorityQueue<>();
        priorityQueue.offer(3);
        priorityQueue.offer(9);
        priorityQueue.offer(7);
        priorityQueue.offer(11);
        priorityQueue.offer(15);
        priorityQueue.offer(13);
        System.out.println(priorityQueue);

        System.out.println("-------------------------");

        MyPriorityQueue<Integer> priorityQueue2 = new MyPriorityQueue<>((a, b) -> b - a);
        priorityQueue2.offer(3);
        priorityQueue2.offer(9);
        priorityQueue2.offer(11);
        priorityQueue2.offer(15);
        priorityQueue2.offer(1);
        System.out.println(priorityQueue2); // 输入可能不会按顺序
    }

}
