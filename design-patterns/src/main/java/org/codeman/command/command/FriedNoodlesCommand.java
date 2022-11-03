package org.codeman.command.command;

import org.codeman.command.cookreceiver.Cook;

/**
 * @author hdgaadd
 * created on 2022/04/25
 */
public class FriedNoodlesCommand extends Command{

    public FriedNoodlesCommand(Cook cook) {
        super(cook);
    }

    @Override
    public void execute() {
        cook.friedNoodles();
    }
}
