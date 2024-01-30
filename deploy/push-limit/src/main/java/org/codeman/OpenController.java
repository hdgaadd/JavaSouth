package org.codeman;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * created on 2022/09/14
 *
 * design: 推送消息 -> 1. 正常调用 || 2. 限流, 写入MQ -> 消费MQ，消息入库 -> 定时任务进行消息重推
 */
@RestController
public class OpenController {

    @Resource
    private InsideService service;

    @GetMapping()
    public void putMsg(String msg) {
        service.pushMsg(msg);
    }

}
