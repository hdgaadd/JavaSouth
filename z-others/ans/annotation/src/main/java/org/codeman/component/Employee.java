package org.codeman.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @author hdgaadd
 * created on 2022/05/28
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employee {
    @NotBlank(message = "please input name")
    private String name;

    @Range(message = "range is {min} to {max}", min = 1, max = 100)
    private Integer age;
}
