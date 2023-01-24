package org.codeman.command;

import org.codeman.command.command.Command;
import org.codeman.command.command.FriedNoodlesCommand;
import org.codeman.command.command.FriedRiceCommand;
import org.codeman.command.cookreceiver.CookReceiverOne;
import org.codeman.command.cookreceiver.CookReceiverTwo;
import org.codeman.command.waiter.Waiter;

/**
 * @author hdgaadd
 * created on 2022/04/25
 *
 * description
 * - 命令队列存储命令，执行命令则遍历队列所有任务
 * - 可撤销某条命令
 *
 * knowledge
 * - 当某基类要求某成员变量必须通过构造方法赋值时，则该基类类型必须选择abstract，而不是interface
 */
public class Client {
    public static void main(String[] args) {
        Waiter waiter = new Waiter();

        Command commandOne = new FriedNoodlesCommand(new CookReceiverOne()); // 要求厨师长One，炒的米粉
        Command commandTwo = new FriedNoodlesCommand(new CookReceiverTwo()); // 要求厨师长Two，炒的米粉
        Command commandThree = new FriedRiceCommand(new CookReceiverTwo()); // 要求厨师长Two，炒的米饭

        waiter.addCommand(commandOne);
        waiter.addCommand(commandTwo);
        waiter.addCommand(commandThree);

        // 取消厨师长Two，炒的米粉
        waiter.cancel(commandTwo);

        waiter.executeCommand();
    }
}
