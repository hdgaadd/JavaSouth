package org.codeman.Stack.VectorRealize;

import java.util.EmptyStackException;

public class MyStack<E> extends MyVector<E> {

    public E peek() {
        int len = size();
        if (len == 0)
            throw new EmptyStackException();
        E element = elementAt(len - 1);
        return element;
    }

    public E pop() {
        int len = size();
        E element = peek();
        removeElementAt(len - 1);
        return element;
    }

    public void push(E e) {
        addElement(e);
    }
}
