package org.codeman;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * created on 2022/11/05
 *
 * design: 存储配置在Redis布隆过滤器 -> 查询布隆过滤器为true，表示该配置存在 -> 将查询过的进行本地缓存 -> 查询数据库相应的配置
 *
 * knowledge: 1. 布隆过滤器实现: 一个超大的初始化位数组，n个哈希函数；元素存入时，每个哈希函数为该元素计算一次得到n个坐标，n个坐标在位数组进行标记为1；查询元素是否存在时，如上进行坐标计算，若每个坐标都是1则表示可能存在（对没有存储过的数据有误判率，以为其存在），若其中一个不为1则表示一定不存在
 *            2. 布隆过滤器可设置误判率大小，越小则计算效率越低，底层实现为增加哈希函数个数
 *            3. Redis的setBit: Redis存储的字符串形式为二进制，该方法是设置key的value在某处的bit值
 */
@RestController
public class OpenController {

    @Resource
    private InsideService service;

    @GetMapping("/setConfigSwitch")
    public String setConfigSwitch(String configVal) {
        service.setConfigSwitch(configVal);
        return "successful!";
    }

    @GetMapping("/getConfigSwitch")
    public boolean getConfigSwitch(String configVal) {
        return service.getConfigSwitch(configVal);
    }

}