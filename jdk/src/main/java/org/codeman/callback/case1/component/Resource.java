package org.codeman.callback.case1.component;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/02/06
 */
@Data
public class Resource {
    List<String> isA = new ArrayList<String>(){{
        add("A");
        add("A");
        add("A");}};

    List<String> isB = new ArrayList<String>(){{
        add("B");
        add("B");
        add("B");}};

    List<String> isC = new ArrayList<String>(){{
        add("C");
        add("C");
        add("C");}};

    List<String> isD = new ArrayList<String>(){{
        add("D");
        add("D");
        add("D");}};

    List<String> isE = new ArrayList<String>(){{
        add("E");
        add("E");
        add("E");}};
}
