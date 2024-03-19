package org.codeman.List.ArrayList;

public class MyOldArraylist {

    private static final int defalut_capacity = 2;

    private int size = 0; // add操作的对应长度

    public int[] elements;

    public MyOldArraylist() {//无参构造方法调用有参构造方法
        this(defalut_capacity);
    }

    public MyOldArraylist(int capacity) {
        elements = new int[capacity];
    }

    public void add(int element) {
        // elements[size++] = element;
        // 把add方法改成调用下一个add方法
        add(size, element);
    }

    // 根据下标添加数据，把大于index的数据都往后移动一位
    public void add(int index, int element) {
        // 处理index
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        capacity();
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1]; // 舍弃掉了最后一个
        }
        elements[index] = element;
        size++;
    }

    // 扩容方法
    public void capacity() {
        if (size < elements.length) {
            return;
        }
        int newCapacity = elements.length + (elements.length >> 1);
        int[] newElements = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements; // elements原来的数组会被回收掉，因为这个数组没有数组引用去指向他
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("size=").append(size).append("[");
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                sb.append(elements[i]);
            } else {
                sb.append(elements[i] + ",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        MyOldArraylist ml = new MyOldArraylist();
        ml.add(1);
        ml.add(2);
        ml.add(3);
        ml.add(0, 6);
        System.out.println(ml.toString());
    }
}
