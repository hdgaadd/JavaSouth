package org.codeman;

import org.codeman.util.CommonResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hdgaadd
 * created on 2022/05/02
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        log.info(CommonResult.success("666").toString());
    }
}
