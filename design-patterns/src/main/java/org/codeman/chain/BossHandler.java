package org.codeman.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/04/19
 */
@Slf4j
public class BossHandler extends AbstractHandler {

    @Override
    public boolean doHandler(Employee employee) {
        if (employee.getVacateDays() <= 3) {
            log.info("审核人可通过的请假天数 <= 3，" + employee.getName() + "审核通过");
            return true;
        } else {
            return false;
        }
    }
}
