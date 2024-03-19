package org.codeman.model;

import java.util.Set;

public class Service {

    private String name;

    private Set<Instance> instances;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Instance> getInstances() {
        return instances;
    }

    public void setInstances(Set<Instance> instances) {
        this.instances = instances;
    }

}
