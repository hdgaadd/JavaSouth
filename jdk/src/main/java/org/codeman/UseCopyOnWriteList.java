package org.codeman;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hdgaadd
 * created on 2023/01/07
 */
public class UseCopyOnWriteList {
    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        list.add(1);
        list.forEach(System.out::print);
    }
}
