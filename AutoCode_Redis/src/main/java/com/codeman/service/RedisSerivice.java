package com.codeman.service;

public interface RedisSerivice {
    public String getKey(String key);
    public void setKey(String key, String val);
    public void expire(String key, int sounds);
}
