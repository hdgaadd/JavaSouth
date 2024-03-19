package org.codeman.channel;

import org.codeman.utils.IntSupplier;

/**
 * @author hdgaadd
 * created on 2022/04/17
 */
public final class DefaultSelectStrategy implements SelectStrategy {

    public static final SelectStrategy INSTANCE = new DefaultSelectStrategy();

    public DefaultSelectStrategy() {

    }

    @Override
    public int calculateStrategy(IntSupplier selectSupplier, boolean hasTasks) throws Exception {
        return hasTasks ? selectSupplier.get() : SELECT;
    }
}
