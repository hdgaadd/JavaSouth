package org.codeman.session;

import java.io.InputStream;

/**
 * @author hdgaadd
 * created on 2022/02/25
 * description: SqlSessionFactory的建造类
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(String fileName) {
        // 获取fileName的输入流：类.class.getClassLoader().getResourceStream()获取的是项目的classpath(Source Root)路径下的资源，一般即java文件夹下的资源文件
        InputStream inputStream = SqlSessionFactory.class.getClassLoader().getResourceAsStream(fileName);
        return build(inputStream);
    }

    public SqlSessionFactory build(InputStream inputStream) {
        // TODO:
        return null;
    }
}
