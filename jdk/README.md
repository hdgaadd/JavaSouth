### automic

- Automic家族保证多线程环境下数据的原子性，相比synchronized更加轻量级，该类操作的是引用类型

### optional

- JDK8为了解决**NPE**问题，参考Google类库中的Optional类设计所创建的工具类

### stringJoiner

- 创造一个字符序列，可添加前缀、后缀、分隔符



### cache

**process**

- @Cacheable可**缓存某方法**的执行结果，只要第二次访问方法所带的参数与第一次**一致**，则直接返回第一次执行的结果

  可应用于性能提高



### custom-exception

**how**

- 创建Assert类抛出异常，需要传递String参数给Assert
- 自定义异常类继承RuntimeException，super()传递异常给RuntimeException
- 或者传递IErrorCode对象给Assert