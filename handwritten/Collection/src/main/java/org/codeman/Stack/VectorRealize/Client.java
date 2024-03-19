package org.codeman.Stack.VectorRealize;

public class Client {
    public static void main(String[] args) {
        MyStack<Integer> myStack = new MyStack<>();
        myStack.push(1);
        myStack.push(2);
        myStack.push(3);
        System.out.println(myStack.peek());
        myStack.pop();
        System.out.println(myStack.peek());
        System.out.println(myStack);
    }
}
