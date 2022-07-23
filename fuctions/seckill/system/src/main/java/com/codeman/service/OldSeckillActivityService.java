package com.codeman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codeman.domain.SeckillActivity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hdgaadd
 * @since 2021-12-09
 */
public interface OldSeckillActivityService extends IService<SeckillActivity> {

    String purchase(Integer activityId);
}
