package com.codeman.stream.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hdgaadd
 * Created on 2022/03/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class People {
    Integer id;

    String name;

    String phone;
}