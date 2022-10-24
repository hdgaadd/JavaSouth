package org.codeman.component;

import com.codeman.domain.SeckillActivity;
import com.codeman.mapper.SeckillActivityMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import util.LOG;

import javax.annotation.Resource;
import java.util.List;

import static com.codeman.constant.RedisKey.STOCK;

/**
 * @author hdgaadd
 * created on 2021/12/10/00:43
 */
@Component
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
        LOG.log("redis初始化成功");
    }
}
