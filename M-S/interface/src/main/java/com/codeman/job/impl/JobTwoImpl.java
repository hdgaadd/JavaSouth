package com.codeman.job.impl;

import com.codeman.job.JobInterface;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * Created on 2022/02/03
 */
@Component
public class JobTwoImpl implements JobInterface {
    @Override
    public void task() {
        System.out.println("--------------------------job-two is running--------------------------");
    }
}
