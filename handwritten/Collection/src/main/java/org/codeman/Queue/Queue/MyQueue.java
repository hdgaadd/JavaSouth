package org.codeman.Queue.Queue;

// LinkedList实现了MyDeque接口，而MyDeque接口继承了MyQueue接口，
// 所以如果MyQueue<Integer> myQueue = new MyLinkedList<>();myQueue对象就只能使用MyQueue接口所特有的方法，而MyLinkedList特有的方法不可使用
public interface MyQueue<E> {

    Boolean add(E element);

    E poll();

    E peek();
}
