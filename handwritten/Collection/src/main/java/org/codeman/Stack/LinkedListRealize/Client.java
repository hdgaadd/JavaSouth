package org.codeman.Stack.LinkedListRealize;

public class Client {
    public static void main(String[] args) {
        MyLinkedList<Integer> myStack = new MyLinkedList<>();
        myStack.push(1);
        myStack.push(2);
        myStack.push(3);
        System.out.println(myStack.peek());
        myStack.pop();
        System.out.println(myStack.peek());
        System.out.println(myStack);

        System.out.println("---------------------------------");

        MyDeque<Integer> myStack2 = new MyLinkedList<>();
        myStack2.push(1);
        myStack2.push(2);
        myStack2.push(3);
        System.out.println(myStack2.peek());
        myStack2.pop();
        System.out.println(myStack2.peek());
        System.out.println(myStack2);
    }
}
