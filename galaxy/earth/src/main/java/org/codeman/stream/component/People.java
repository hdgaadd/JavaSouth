package org.codeman.stream.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author hdgaadd
 * created on 2022/03/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class People {
    Integer id;

    String name;

    String phone;
}