package org.codeman.command.waiter;

import org.codeman.command.command.Command;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

/**
 * @author hdgaadd
 * created on 2022/04/25
 */
@Slf4j
public class Waiter {
    private Queue<Command> queue;

    public void addCommand(Command command) {
        if (!Objects.isNull(command)) {
            (this.queue = Optional.ofNullable(queue).orElse(new LinkedList<>())).offer(command); // 使用Optional
        }
    }

    public void executeCommand() {
        (this.queue = Optional.ofNullable(queue).orElse(new LinkedList<>())).forEach(item -> item.execute());
    }

    public void cancel(Command command) {
        boolean isRemove = (this.queue = Optional.ofNullable(queue).orElse(new LinkedList<>())).remove(command);
        String message = isRemove ? "取消命令成功" : "取消命令失败";
        log.info(message);
    }
}
