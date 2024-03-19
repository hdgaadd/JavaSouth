package org.codeman.factory;

import org.codeman.AbstractLoadBalance;
import org.codeman.model.CustomInstance;
import org.codeman.subclasses.CustomLoadBalance;

/**
 * @author hdgaadd
 * created on 2023/01/20
 */
public class CustomLoadBalanceFactory implements LoadBalanceAbstractFactory {

    @Override
    public AbstractLoadBalance<CustomInstance> getLoadBalance() {
        return new CustomLoadBalance();
    }

}
