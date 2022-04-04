package com.codeman.callback.caseTwo.component;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * Created on 2022/02/06
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
}
