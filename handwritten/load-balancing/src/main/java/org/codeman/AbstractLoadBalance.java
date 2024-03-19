package org.codeman;

import org.codeman.model.Instance;

import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/10/25
 */
public abstract class AbstractLoadBalance<E extends Instance> implements LoadBalance<E> {

    protected abstract Instance doLoad(List<E> instances);

    // strictly specify the things that subclasses must be done
    public Instance load(final List<E> instances) {
        if (instances == null || instances.size() == 0) {
            return null;
        }
        if (instances.size() == 1) {
            return instances.get(0);
        }
        return doLoad(instances);
    }

}
