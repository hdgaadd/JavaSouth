package com.condemn.service;

public interface RedisService {

    public String getKey(String key);

    public void setKey(String key, String val);

    public void expire(String key, int sounds);

}
