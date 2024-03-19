package org.codeman;

import org.codeman.factory.CustomLoadBalanceFactory;
import org.codeman.factory.RandomLoadBalanceFactory;
import org.codeman.model.CustomInstance;
import org.codeman.model.RandomInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/10/25
 *
 * design: 采用java.security的SecureRandom实现
 *
 * description: 抽象工厂LoadBalanceAbstractFactory -> 工厂方法模式AbstractLoadBalance
 */
public class Client {

    private static final List<CustomInstance> customInstances = new ArrayList<CustomInstance>(){{
        add(new CustomInstance("127.0.0.1"));
        add(new CustomInstance("127.0.0.2"));
        add(new CustomInstance("127.0.0.3"));
    }};

    private static final List<RandomInstance> randomInstances = new ArrayList<RandomInstance>(){{
        add(new RandomInstance("127.0.0.1"));
        add(new RandomInstance("127.0.0.2"));
        add(new RandomInstance("127.0.0.3"));
    }};

    public static void main(String[] args) {
        RandomLoadBalanceFactory randomFactory = new RandomLoadBalanceFactory();
        AbstractLoadBalance<RandomInstance> random = randomFactory.getLoadBalance();
        for (int i = 0; i < 6; i++) {
            System.out.println(random.load(randomInstances));
        }

        CustomLoadBalanceFactory customFactory = new CustomLoadBalanceFactory();
        AbstractLoadBalance<CustomInstance> custom = customFactory.getLoadBalance();
        for (int i = 0; i < 6; i++) {
            System.out.println(custom.load(customInstances));
        }
    }
}
