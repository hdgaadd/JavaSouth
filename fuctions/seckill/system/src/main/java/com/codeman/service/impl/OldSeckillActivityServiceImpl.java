package com.codeman.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeman.domain.SeckillActivity;
import com.codeman.mapper.SeckillActivityMapper;
import com.codeman.service.OldSeckillActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hdgaadd
 * @since 2021-12-09
 */
@Service
public class OldSeckillActivityServiceImpl extends ServiceImpl<SeckillActivityMapper, SeckillActivity> implements OldSeckillActivityService {
    @Resource
    private SeckillActivityMapper seckillActivityMapper;
    static int count = 0;
    @Override
    public String purchase(Integer activityId) {
        SeckillActivity seckillActivity = seckillActivityMapper.selectById(activityId);
        int totalStock = seckillActivity.getTotalStock();
        System.out.println("now is point" + count + ", totalstock is " + totalStock);
        if (totalStock > 1) {
            count++;
            totalStock -= 1;
            seckillActivity.setTotalStock(totalStock);
            seckillActivityMapper.update(seckillActivity, null);
            System.out.println("purchase succeed");
            return "purchase succeed";
        } else {
            System.out.println("sorry, it is out of stock");
            return "sorry, it is out of stock";
        }
    }
}
