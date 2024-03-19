package org.codeman.Map.HashMap;

 // 底层
 // Entry数组，默认长度为8，添加数据时根据key的hashCode找到下标并插入
 // 链表：新加入的map插在旧头部的上面，把有相同下标的key通过next联系起来
public class OldHashMap<K, V> {

    private Entry<K, V>[] table; // Entry对象的数组

    private static final Integer CAPACITY = 8; // 默认Entry数组的大小

    private Integer size = 0;

    class Entry<K, V> { // Entry对象

        private K key;

        private V val;

        private Entry<K, V> next; // next的类型是Entry对象

        public Entry(K key, V val, Entry next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getVal() {
            return val;
        }
    }

    public void put(K key, V val) {
        if (table == null) {
            inflate();
        }

        Integer hasCode = key.hashCode();
        Integer index = indexFor(hasCode); // 计算加入map的下标,根据key的值计算出下标
        /*addEntry原方法Entry<K,V> entry=new Entry<>(key,val,table[index]);
                       table[index]=entry;*/

        // key相同的话,update
        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if (entry.key.equals(key)) {
                entry.val = val;
            }
        }
        // key不相同,下标相同
        addEntry(new Entry<>(key, val, table[index]), index);
    }

    private void addEntry(Entry<K, V> entry1, Integer index) {
        Entry<K, V> entry = entry1;
        table[index] = entry;
        size++;
    }

    private int indexFor(Integer hasCode) { // 抽出这个方法
        return hasCode % table.length;
    }

    private void inflate() {
        table = new Entry[CAPACITY];
    }

    public V get(K key) {
        Integer hasCode = key.hashCode(); // 使用hashCode()获取hashCode
        Integer index = indexFor(hasCode);
        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if (entry.key.equals(key)) { // 使用equals()处理key
                return entry.val;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        OldHashMap<Integer, Integer> myHashMap = new OldHashMap<>();
        myHashMap.put(1, 66);
        System.out.println(myHashMap.get(1));
    }

}
