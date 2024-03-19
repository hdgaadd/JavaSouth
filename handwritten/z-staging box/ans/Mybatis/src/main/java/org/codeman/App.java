package org.codeman;

import org.codeman.session.SqlSession;
import org.codeman.session.SqlSessionFactory;
import org.codeman.session.SqlSessionFactoryBuilder;

/**
 * @author hdgaadd
 * created on 2022/04/29
 */
public class App {
    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build("conf.properties");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // TODO: 
    }
}
