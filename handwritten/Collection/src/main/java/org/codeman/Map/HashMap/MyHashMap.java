package org.codeman.Map.HashMap;

import java.util.HashMap;
import java.util.Map;

// jdk1.7没有node是因为Entry对象有next属性
// jdk1.8
// 每一个HashMap都是一个Entry对象,每一个HashMap都是一个Node
// Node<K,V> implements org.codeman.Map.Entry<K,V>,Node就是一个Entry,把Entry换了一个名
// jdk7是从上往下添加node,jdk8是从下往上添加node
// 对于下标，如果是整型，相同位数一般不在同一下标，不同位数在同一下标的几率更大
public class MyHashMap<K, V> {

    int TREEIFY_THRESHOLD = 8; // 链表长度

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // Entry数组长度

    Node<K, V>[] table;

    class Node<K, V> implements Map.Entry {

        K key;

        V value;

        int hash;

        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public Object getKey() {
            return null;
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public Object setValue(Object value) {
            return null;
        }
    }

    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    // hash
    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public void putVal(int hash, K key, V value) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, i; // p是该下标的第一个元素
        if ((tab = table) == null || (n = tab.length) == 0) { // Entry数组为空，new Entry数组
            n = (tab = resize()).length; // 刚开始tab和table都为null,重新创建table数组后，要tab=table
        }
        if ((p = tab[i = ((n - 1) & hash)]) == null) { // 通过(n-1)&hash找到下标     // 该下标为空，直接添加
            tab[i] = new Node<>(hash, key, value, null);
        } else { // p是下标的第一个元素，e是p的下一个元素
            Node<K, V> e;
            if (p.hash == hash && p.key == key) { // 要添加的数据与该下标的node的key相同，覆盖
                e = p; // e和p的内存空间地址指向相同了
            } else {
                for (int binCount = 0; ; binCount++) { // 不断遍历，直到链表的下一个为空，可以添加数据
                    if ((e = p.next) == null) { // 链表的下一个为空，添加数据
                        p.next = new Node<>(hash, key, value, null);
                        // if(binCount>=TREEIFY_THRESHOLD-1) // 如果超过8个，链表转换为红黑树，新的数据已经添加上去了，此时链表有9个元素
                        // treeifyBin(tab,hash); // 把链表转换为红黑树
                        break;
                    }
                    if (e.hash == hash && e.key == key) { // 要添加的数据与当前链表的key相同，覆盖
                        break;
                    }
                    p = e; // 让循环继续
                }
            }
            if (e != null) { // 覆盖
                V oldVaulue = e.value;
                e.value = value; // e和p的内存空间地址指向相同了，e的value改变，p的value也会改变，=覆盖
            }
        }

    }

    public Node<K, V>[] resize() { // 创建数组
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        return table;
    }

    public V get(K key) { // 自己思路的get
        Node<K, V> p;
        int n = table.length;
        int hash = hash(key);
        int count = 0;
        if ((p = table[(n - 1) & hash]) == null) {
            return p.value;
        } else {
            while (p != null) {
                if (p.key == key || p.key.equals(key)) { // 当是13时，key和p.key的地址一样，当是1117时，key和p.key的地址不一样一样
                    return p.value; // 官方写了==或equals都可以，((k = e.key) == key || (key != null && key.equals(k))))
                }
                p = p.next;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        /*MyHashMap<Integer, Integer> myHashMap = new MyHashMap<>();
        System.out.println("下标为:");
        System.out.println(15&(myHashMap.hash( 1117)));
        System.out.println(15&(myHashMap.hash( 13)));
        System.out.println("------------------");
        myHashMap.put(13,3);
        myHashMap.put(1117,7);
        System.out.println(myHashMap.get(13));
        System.out.println(myHashMap.get(1117));*/

        long start, end;
        start = System.currentTimeMillis();
        MyHashMap<Integer, Integer> myHashMap = new MyHashMap<>();
        for (int i = 0; i < 10000; i++) {
            myHashMap.put(i, i);
        }
        end = System.currentTimeMillis();
        System.out.println(myHashMap.get(766));
        System.out.println("运行时间：" + (end - start) + "(ms)");
    }

}
