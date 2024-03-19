package org.codeman.subclasses;

import org.codeman.AbstractLoadBalance;
import org.codeman.model.Instance;
import org.codeman.model.RandomInstance;

import java.security.SecureRandom;
import java.util.List;

/**
 * @author hdgaadd
 * created on 2022/10/25
 */
public class RandomLoadBalance extends AbstractLoadBalance<RandomInstance> {

    SecureRandom RANDOM = new SecureRandom();

    public Instance doLoad(final List<RandomInstance> instances) {
        return instances.get(RANDOM.nextInt(instances.size()));
    }
}
