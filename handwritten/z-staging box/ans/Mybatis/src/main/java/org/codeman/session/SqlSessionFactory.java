package org.codeman.session;

/**
 * @author hdgaadd
 * created on 2022/02/25
 */
public interface SqlSessionFactory {

    SqlSession openSession();
}
