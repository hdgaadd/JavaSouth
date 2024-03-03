package org.codeman.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hdgaadd
 * created on 2023/01/08
 *
 * - FIFO：先进先出
 * - LRU：最近最少使用（晚put的放队尾，get元素时放到队尾，每次删除队首元素）（应对稍微过时的热点流量不好）
 * - LFU：最近最少频率使用（根据最少频率删除元素）（应对突发流量支持不好）
 */
public class LRU {

    public static void main(String[] args) {
        LRUMap map = new LRUMap(3);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        System.out.println(map);

        map.get(2);
        System.out.println(map);
    }

    private static class LRUMap extends LinkedHashMap {
        private int max;

        public LRUMap(int max) {
            // accessOrder设置get时，把该get的元素放到队尾
            super((int) (max * 1.4f), 1.75f, true);
            this.max = max;
        }

        /**
         * 容量满时，删除队首元素
         *
         * @param eldest
         * @return
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > max;
        }
    }
}
