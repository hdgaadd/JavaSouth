package org.codeman.chain.objs;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author hdgaadd
 * Created on 2022/04/19
 * @Description 组长
 */
@Slf4j
public class Boss extends Handler {

    @Override
    public void doHandler(Employee employee) {
        if (employee.getVacateDays() <= 3) {
            log.info("审核人可通过的请假天数<=3，" + employee.getEmployeeName() + "审核通过");
        } else {
            if (Objects.isNull(getNextHandler())) {
                log.info("审核人可通过的请假天数<=3，" + employee.getEmployeeName() + "审核失败");
            } else {
                getNextHandler().doHandler(employee);
            }
        }
    }
}
