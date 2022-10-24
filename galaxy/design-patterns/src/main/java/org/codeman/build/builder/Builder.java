package org.codeman.build.builder;

/**
 * @author hdgaadd
 * created on 2022/10/04
 */
public abstract class Builder<T> {

    public T t;

    public abstract void setServer();

    public T getServer() { return t; };
}
