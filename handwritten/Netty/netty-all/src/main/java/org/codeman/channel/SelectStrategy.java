package org.codeman.channel;

import org.codeman.utils.IntSupplier;

/**
 * @author hdgaadd
 * created on 2022/04/13
 */
public interface SelectStrategy {
    int SELECT = -1;

    int CONTINUE = -2;

    int BUSY_WAIT = -3;

    int calculateStrategy(IntSupplier selectSupplier, boolean hasTasks) throws Exception; // [ˈkælkjuleɪt]计算策略

}
