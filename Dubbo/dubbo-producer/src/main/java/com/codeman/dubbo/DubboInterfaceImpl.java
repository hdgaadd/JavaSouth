package com.codeman.dubbo;

import com.codeman.dubbo.DubboInterface;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author hdgaadd
 * Created on 2022/01/25
 */
@DubboService(version = "${dubbo.application.version}", timeout = 5000)
public class DubboInterfaceImpl implements DubboInterface {
    @Override
    public String communicate() {
        return "welcome to learning Dubbo";
    }
}
