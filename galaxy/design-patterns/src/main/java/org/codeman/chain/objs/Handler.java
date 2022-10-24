package org.codeman.chain.objs;


import lombok.Data;

/**
 * @author hdgaadd
 * created on 2022/04/18
 */
@Data
public abstract class Handler {
    private Handler nextHandler;

    public abstract void doHandler(Employee employee);
}
