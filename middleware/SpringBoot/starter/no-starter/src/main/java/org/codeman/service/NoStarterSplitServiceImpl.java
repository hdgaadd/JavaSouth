package org.codeman.service;

import java.util.Arrays;

/**
 * @author hdgaadd
 * created on 2022/12/17
 */
public class NoStarterSplitServiceImpl implements INoStarterSplitService {

    @Override
    public String split(String value) {
        return Arrays.toString(value.split(" "));
    }
}
