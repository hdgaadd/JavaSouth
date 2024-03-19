package org.codeman.model;


import lombok.Data;

import java.util.Map;
import java.util.Objects;

@Data
public class Instance {

    private String ip;

    private int port;

    private Map<String, String> metaData;

    private long heartbeatTime;

    private String serviceName;

    @Override
    public String toString() {
        return "Instance{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", metaData=" + metaData +
                ", heartbeatTime=" + heartbeatTime +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instance instance = (Instance) o;
        return port == instance.port && Objects.equals(ip, instance.ip) && Objects.equals(serviceName, instance.serviceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, serviceName);
    }
}
