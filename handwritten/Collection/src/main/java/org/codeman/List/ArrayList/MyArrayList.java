package org.codeman.List.ArrayList;

import java.util.Arrays;

public class MyArrayList<E> {

    transient Object[] elementData;

    /**
     * 当前元素个数，也代表下标
     */
    int size;

    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    private static final int DEFAULT_CAPACITY = 10;

    public MyArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    public boolean add(E element) {
        ensureCapacityInternal(size + 1);
        elementData[size++] = element;
        return true;
    }

    public void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(minCapacity));
    }

    public int calculateCapacity(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        return minCapacity;
    }

    public void ensureExplicitCapacity(int minCapacity) {
        if (minCapacity > elementData.length)
            grow(minCapacity);
    }

    public void grow(int minCapacity) {
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + oldCapacity >> 1; // 1.5倍
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity; // 如果数组为空创建默认数组为10的数组
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("size = ").append(size).append(" , array = ").append("[");
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                sb.append(elementData[i]);
            } else {
                sb.append(elementData[i] + ",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        MyArrayList<Integer> ml = new MyArrayList<>();
        ml.add(1);
        ml.add(2);
        ml.add(3);
        System.out.println(ml.toString());
    }

}
