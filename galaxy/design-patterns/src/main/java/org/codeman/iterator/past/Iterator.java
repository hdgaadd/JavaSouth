package org.codeman.iterator.past;

import java.util.ArrayList;
import java.util.List;

//迭代器
public class Iterator {
    public static void main(String[] args) {

    }
}

class SaveArray {
    int[] array=new int[]{1,2,3};
}

class SaveList{
    List<Integer> list=new ArrayList<Integer>(){{
        add(5);add(6);add(7);
    }};
    public Iterator createIterator(){
        return (Iterator) list.iterator();
    }
}
