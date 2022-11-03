package org.codeman.command.cookreceiver;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/04/25
 */
@Slf4j
public class CookReceiverOne extends Cook {
    public void friedNoodles() { //  [fraɪd]
        log.info("厨师长One：香喷喷的炒米粉开始制作咯");
    }

    public void friedRice() {
        log.info("厨师长One：香喷喷的炒饭开始制作咯");
    }
}
