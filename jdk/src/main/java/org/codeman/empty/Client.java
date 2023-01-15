package org.codeman.empty;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hdgaadd
 * created on 2022/07/10
 *
 * description: org.apache.commons.langs
 */
public class Client {

    private static final List<Integer> list = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println(Objects.isNull(list));
        System.out.println(ObjectUtils.isEmpty(list));
        System.out.println(ObjectUtils.isNotEmpty(list));
    }
}
