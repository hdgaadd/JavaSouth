package org.codeman.Stack.LinkedListRealize;

public interface MyDeque<E> {

    // 当Deque<Integer> dequeStack=new LinkedList<>()时，规范栈的方法
    void push(E e);

    E peek();

    E pop();
}
