package org.codeman;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.codeman.service.SeckillService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2021/12/13
 *
 * design: Redis + Lua解决秒杀库存超卖现象 -> RocketMQ解决实际库存超卖现象
 *
 * knowledge: RocketMQ的功能，可使用事务代替
 */
@RestController
@Slf4j
public class OpenController {
    @Resource
    private SeckillService seckillService;

    @PostConstruct
    public void sentinelFlow() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("seckill-resource");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(1);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    @GetMapping("/skill/{activityId}/{memberId}")
    @ApiOperation("根据活动id秒杀 -> 创建订单 -> 返回订单编号")
    public String skill(@PathVariable Long activityId, @PathVariable Long memberId) throws Exception {
        try (Entry entry = SphU.entry("seckill-resource")) {
            Boolean result = seckillService.skill(activityId, memberId);

            String orderCode = null;
            if (result) {
                orderCode = seckillService.createOrder(activityId, memberId);
            }
            return orderCode != null ? "秒杀成功，订单编号为：" + orderCode : "秒杀失败，库存不足或您已经秒杀过了";
        } catch (BlockException blockException) {
            return "bro, slow down!";
        }
    }

    @GetMapping("payOrder/{orderCode}")
    @ApiOperation("支付订单")
    public String payOrder(@PathVariable String orderCode) throws Exception {
        return seckillService.payOrder(orderCode);
    }

}
