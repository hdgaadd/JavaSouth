package org.codeman.command;

import org.codeman.command.command.Command;
import org.codeman.command.command.FriedNoodlesCommand;
import org.codeman.command.command.FriedRiceCommand;
import org.codeman.command.cookreceiver.CookReceiverOne;
import org.codeman.command.cookreceiver.CookReceiverTwo;
import org.codeman.command.waiter.Waiter;

/**
 * @author hdgaadd
 * Created on 2022/04/25
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
