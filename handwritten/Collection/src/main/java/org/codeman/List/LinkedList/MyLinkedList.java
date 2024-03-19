package org.codeman.List.LinkedList;

public class MyLinkedList<E> {

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

    public void addFirst(E element) {
        linkFirst(element);
    }

    public void addLast(E element) {
        linkLast(element);
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

    public int size() {
        return size;
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

    public static void main(String[] args) {
        MyLinkedList<Integer> myLinkedList = new MyLinkedList<>();
        myLinkedList.addFirst(1);
        myLinkedList.addLast(2);
        myLinkedList.addFirst(3);//312
        System.out.println(myLinkedList.toString());
        System.out.println(myLinkedList.size());
    }

}
