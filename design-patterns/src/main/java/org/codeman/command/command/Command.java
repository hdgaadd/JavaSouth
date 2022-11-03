package org.codeman.command.command;

import org.codeman.command.cookreceiver.Cook;

/**
 * @author hdgaadd
 * created on 2022/04/25
 */
public abstract class Command { // 当某基类要求某成员变量必须通过构造方法赋值时，则该基类类型必须选择abstract，而不是interface
    protected final Cook cook;

    public Command(Cook cook) {
        this.cook = cook;
    }

    public abstract void execute();
}
