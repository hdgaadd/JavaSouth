package org.codeman.component.repository;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author hdgaadd
 * created on 2022/09/28
 */
@Data
@Accessors(chain = true)
public class Clock implements Serializable {

    private String time;

}