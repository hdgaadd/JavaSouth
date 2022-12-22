package org.learn;

import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/22
 */
@Component
public class LearnAutoConfigureBefore {
    static {
        System.out.println("LearnAutoConfigureBefore is start!");
    }
}
