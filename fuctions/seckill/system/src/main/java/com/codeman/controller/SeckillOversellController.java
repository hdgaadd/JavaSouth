package com.codeman.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.codeman.service.SeckillOversellService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.LOG;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * Created on 2021/12/10 00:00:11
*/
@Api(tags = "秒杀")
@RestController
@RequestMapping("/seckill")
// oversale售空[ˌəʊvəˈsel]
public class SeckillOversellController {
    @Resource
    private SeckillOversellService seckillOversellService;

    @GetMapping("/stockDeductVaildator/{activityId}/{memberId}")
    @ApiOperation("根据活动id，秒杀，创建订单，返回订单编号")
    public String stockDeductVaildator(@PathVariable Long activityId, Long memberId) throws Exception {
        try (Entry entry = SphU.entry("seckillResource")) {
            Boolean result = seckillOversellService.stockDeductVaildator(activityId, memberId);
            String orderCode = null;
            if (result) {
                orderCode = seckillOversellService.createOrder(activityId, memberId);
            }
            return orderCode != null ? "秒杀成功，订单编号为：" + orderCode : "秒杀失败，库存不足或您已经秒杀过了";
        } catch (BlockException blockException) {
            LOG.log("限流");
            return "慢点，兄弟";
        }
    }

    @PostConstruct
    public void sentinelFlow() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("seckillResource");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(1);

        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
