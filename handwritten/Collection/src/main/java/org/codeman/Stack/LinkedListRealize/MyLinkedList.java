package org.codeman.Stack.LinkedListRealize;

public class MyLinkedList<E> implements MyDeque<E> {

    int size = 0;

    Node<E> first;

    Node<E> last;

    class Node<E> {
        E element;
        Node<E> prev;
        Node<E> next;

        public Node(Node<E> prev, E element, Node<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }
    }

    public void linkFirst(E element) {
        // 根据设定:第一个节点是first最后一个节点是last
        Node f = first; // 临时变量
        Node<E> newNode = new Node<E>(null, element, f);
        first = newNode;
        if (f == null) // 有临时变量此时f和first不会覆盖
            last = newNode; // 因为上面的first=newNode;第一个添加的元素既是first也是last
        else
            f.prev = newNode;
        size++;
    }

    public void linkLast(E element) {
        Node l = last; // 临时变量
        Node<E> newNode = new Node<E>(l, element, null); // l或last都可
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node temp = first;
        while (temp != null) {
            sb.append(temp.element + ",");
            temp = temp.next;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    public int size() {
        return size;
    }

    /**-------------------------------------stack方法-------------------------------------**/
    public void push(E element) {
        linkFirst(element);
    }

    public E pop() {
        Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f); // 传输first节点
    }

    public E peek() {
        Node<E> f = first;
        E element = first.element;
        return f == null ? null : element;
    }

    // 出栈
    public E unlinkFirst(Node<E> f) {
        E element = f.element;
        Node next = f.next;
        // 把f对象清空
        f.element = null;
        f.next = null;
        first = next;
        if (next == null)
            last = null; // 没有元素了，last也必须为null
        else
            next.prev = null;
        size--;
        return element;
    }
    /**-------------------------------------stack方法-------------------------------------**/
}
