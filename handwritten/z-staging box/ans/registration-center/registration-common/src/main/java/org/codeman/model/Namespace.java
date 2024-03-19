package org.codeman.model;

import java.util.Set;

public class Namespace {
    private String name;

    private Set<Service> services;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }
}
