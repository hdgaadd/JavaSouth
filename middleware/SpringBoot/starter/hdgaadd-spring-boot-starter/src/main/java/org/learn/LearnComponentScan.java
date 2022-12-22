package org.learn;

import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * created on 2022/12/22
 */
@Component
public class LearnComponentScan {
    static {
        System.out.println("LearnComponentScan is register!");
    }
}
