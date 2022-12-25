package org.codeman.chain.objs;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author hdgaadd
 * created on 2022/04/19
 *
 * description: 组长
 */
@Slf4j
public class GroupManager extends Handler {

    @Override
    public void doHandler(Employee employee) {
        if (employee.getVacateDays() <= 2) {
            log.info("审核人可通过的请假天数 <= 2，" + employee.getEmployeeName() + "审核通过");
        } else {
            if (Objects.isNull(getNextHandler())) {
                log.info("审核人可通过的请假天数 <= 2，" + employee.getEmployeeName() + "审核失败");
            } else {
                getNextHandler().doHandler(employee);
            }
        }
    }
}
