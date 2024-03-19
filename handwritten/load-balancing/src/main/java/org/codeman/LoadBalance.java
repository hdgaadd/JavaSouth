package org.codeman;

import org.codeman.model.Instance;

import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/10/25
 */
public interface LoadBalance<E extends Instance> {
    Instance load(List<E> instances);
}
