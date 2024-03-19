package org.codeman.factory;

import org.codeman.AbstractLoadBalance;
import org.codeman.model.RandomInstance;
import org.codeman.subclasses.RandomLoadBalance;

/**
 * @author hdgaadd
 * created on 2023/01/20
 */
public class RandomLoadBalanceFactory implements LoadBalanceAbstractFactory {

    @Override
    public AbstractLoadBalance<RandomInstance> getLoadBalance() {
        return new RandomLoadBalance();
    }
}
