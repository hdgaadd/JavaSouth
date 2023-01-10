package org.codeman.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/04/19
 */
@Slf4j
public class SuperBossHandler extends AbstractHandler {

    @Override
    public boolean doHandler(Employee employee) {
        if (employee.getVacateDays() <= 5) {
            log.info("审核人可通过的请假天数 <= 5，" + employee.getName() + "审核通过");
            return true;
        } else {
            return false;
        }
    }
}
