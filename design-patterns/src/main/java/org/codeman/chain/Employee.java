package org.codeman.chain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hdgaadd
 * created on 2022/04/18
 */
@Data
@AllArgsConstructor
public class Employee { // [ɪmˈplɔɪiː]

    private String name;

    /**
     * 请假天数
     */
    private Integer vacateDays;

}
