package org.codeman.manage;

import org.codeman.model.Instance;

import java.util.Set;

public interface ParrotManage {

    void addNamespace(String namespace);

    void deleteNamespace(String namespace);

    Set<String> getNamespaces();

    void addInstance(String namespace, Instance instance);

    void deleteInstance(String namespace, Instance instance);

    Set<Instance> getInstance(String namespace, String service);

    Set<String> getServices(String namespace);

    void deleteService(String namespace, String service);

}
