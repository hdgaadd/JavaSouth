package org.codeman.factory;

import org.codeman.LoadBalance;
import org.codeman.model.Instance;

/**
 * @author hdgaadd
 * created on 2023/01/20
 */
public interface LoadBalanceAbstractFactory {
    LoadBalance<? extends Instance> getLoadBalance();
}
