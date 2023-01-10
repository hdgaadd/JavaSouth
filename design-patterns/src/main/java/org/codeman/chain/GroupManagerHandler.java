package org.codeman.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/04/19
 * <p>
 * description: 组长
 */
@Slf4j
public class GroupManagerHandler extends AbstractHandler {

    @Override
    public boolean doHandler(Employee employee) {
        if (employee.getVacateDays() <= 2) {
            log.info("审核人可通过的请假天数 <= 2，" + employee.getName() + "审核通过");
            return true;
        } else {
            return false;
        }
    }
}
