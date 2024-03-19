package org.codeman.Queue.Queue;

public class Client {
    public static void main(String[] args) {
        MyQueue<Integer> myQueue = new MyLinkedList<>();
        myQueue.add(1);
        myQueue.add(2);
        myQueue.add(3);
        System.out.println(myQueue);
        System.out.println(myQueue.peek());
        System.out.println(myQueue.poll());
        System.out.println(myQueue.poll());
        System.out.println(myQueue);
    }
}
