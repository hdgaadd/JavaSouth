package com.codeman.stream.component;

/**
 * @author hdgaadd
 * Created on 2022/03/22
 */
public class User {
    Integer id;

    public User(Integer id) {
        this.id = id;
    }

    public User() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}