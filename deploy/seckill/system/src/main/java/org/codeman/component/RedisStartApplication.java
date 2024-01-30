package org.codeman.component;

import org.codeman.entity.SeckillActivity;
import org.codeman.mapper.SeckillActivityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static org.codeman.constant.RedisKey.STOCK;

/**
 * @author hdgaadd
 * created on 2021/12/10
 */
@Component
@Slf4j
public class RedisStartApplication implements ApplicationRunner {
    @Resource
    private RedisService redisService;
    @Resource
    private SeckillActivityMapper seckillActivityMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SeckillActivity> seckillActivities = seckillActivityMapper.selectList(null);
        for (SeckillActivity activity : seckillActivities) {
            redisService.setKey(STOCK.toString() + activity.getId(), activity.getAvailableStock());
        }
        log.info("redis初始化成功");
    }
}
