### ClassLoader

**process**

- 重写findClass, 将类自定义加载成byte[]后，传递给defindClass

**knowledge**

- **将.class文件转换为jar**

  ```
  cd C:\Java\jdk1.8.0_311\bin
  
  jar cvf Hello.jar Hello.class
  ```

- **springboot实现加载**

  - 加载: 将jar传递给ApplicationContext进行加载
  - 卸载: 关闭ApplicationContext、清除ClassLoader、清除jar包