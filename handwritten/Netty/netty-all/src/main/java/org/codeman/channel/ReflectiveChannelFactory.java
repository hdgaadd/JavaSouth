package org.codeman.channel;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;

/**
 * @author hdgaadd
 * created on 2022/04/13
 */
@Getter
@Setter
public class ReflectiveChannelFactory<T extends Channel> {

    private Constructor<T> constructor;

    public ReflectiveChannelFactory(Class<T> clazz) {
        try {
            this.constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public T newChannel() {
        try {
            return constructor.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException("Unable to create Channel from class " + constructor.getDeclaringClass());
        }
    }
}
