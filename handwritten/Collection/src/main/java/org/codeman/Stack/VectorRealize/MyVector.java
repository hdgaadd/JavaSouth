package org.codeman.Stack.VectorRealize;

import java.util.Arrays;

public class MyVector<E> {

    int elementCount = 0; // 当前元素个数，也代表下标，和ArrayList一致

    int modCount = 0; // 数组修改次数

    Object[] elementData;

    public MyVector() {
        this(10); // initialCapacity初始容量
    }

    public MyVector(int initialCapacity) {
        if (initialCapacity < 0)
            System.out.println("sb");
        this.elementData = new Object[initialCapacity]; // Object
    }

    public synchronized boolean add(E e) { // 同步的
        modCount++;
        ensureCapacityHelper(elementCount + 1); // 确保数组容量足够
        elementData[elementCount++] = e;
        return true;
    }

    public void ensureCapacityHelper(int minCapacity) { // minCapacity代表如果加了一个元素后，当前数组当前元素个数
        if ((minCapacity - elementData.length) > 0) {
            grow(); // 扩容
        }
    }

    private void grow() {
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + oldCapacity; // 2倍
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    /** -------------------------------------stack方法------------------------------------- **/
    // 入栈
    public synchronized void addElement(E e) {
        modCount++;
        ensureCapacityHelper(elementCount + 1); // 确保数组容量足够
        elementData[elementCount++] = e;
    }

    // 出栈
    public synchronized void removeElementAt(int index) {
        modCount++;
        if (index < 0)
            throw new ArrayIndexOutOfBoundsException("出界了哥");
        elementCount--;
        elementData[index] = null;
    }

    public synchronized E elementAt(int index) {
        return (E) elementData[index];
    }

    /** -------------------------------------stack方法------------------------------------- **/

    public int size() {
        return elementCount;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("size=").append("[");
        for (int i = 0; i < elementCount; i++) {
            if (i == elementCount - 1) {
                sb.append(elementData[i]);
            } else {
                sb.append(elementData[i] + ",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
