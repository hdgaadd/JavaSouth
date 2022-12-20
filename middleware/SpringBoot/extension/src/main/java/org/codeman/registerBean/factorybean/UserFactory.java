package org.codeman.registerBean.factorybean;

import org.codeman.registerBean.component.User;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author hdgaadd
 * created on 2022/12/20
 */
public class UserFactory implements FactoryBean<User> {

    @Override
    public User getObject() throws Exception {
        return new User().setName("hdgaadd");
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
