### Mybatis

**knowledge**

- 类.class.getClassLoader().getResourceStream()获取的是项目的classpath(**Source Root**)路径下的资源，一般即java文件夹下的资源文件

  ```
  InputStream inputStream = SqlSessionFactory.class.getClassLoader().getResourceAsStream(fileName);
  ```
