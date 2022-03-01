# structure

```lua
universe
├── async -- 异步执行
├── autoCode-Redis -- 验证码
├── bean-mapping -- bean映射
├── cache -- 方法结果缓存
├── custom-exception -- 自定义异常
├── Dubbo
     ├── dubbo-interface -- 远程接口
     ├── dubbo-producer -- 远程服务
     └── dubbo-web -- 本地服务
├── Gateway 
     ├── gateway-client -- 被网关包裹的服务
     └── gateway-server -- 网关服务端
├── JWT -- 认证授权
     ├── part-one -- 旧版本
     └── part-two
├── knowledge
├── log
     ├── log4j
     └── slf4j
├── M-S -- 可扩展性、可维护性
     ├── arguments -- 方法参数个数
     ├── call-back -- 回调函数
     ├── entity -- 实体类公有财产
     └── interface -- 接口运用
├── monitor -- 监视器
     ├── springboot-client -- 被监视者
     └── springboot-server -- SpringBoot Admin服务端
├── order-task -- 定时任务
├── Redisson -- 分布式锁
     ├── server-one -- 旧版本
     └── server-two
├── returnR -- 统一结果集
├── RocketMQ
├── ShardingSphere -- 读写分离，单库分表
├── template -- 项目模板
├── trigger-log -- 日志触发
├── util -- 生成器
├── xxl-job -- 分布式任务
     └── core
└── z-dp
```



# async

> asynchronous异步的[eɪˈsɪŋkrənəs]

**process**

- 使用@Async可以使用方法线程与程序主线程，**异步执行**

  主线程不用**等**该方法执行完再执行方法下的逻辑

# autoCode-Redis

**HOW**

- for循环联合Random生成验证码，保存在Redis里

**bugs**

- ```
  java.lang.NullPointerException: null空指针异常
  ```

  sloved：private RedisService redisSerivice没有添加@Resource

  定位应该是对象，却测试变量，变量不可能空指针异常





# bean-mapping

# cache

**process**

- @Cacheable可**缓存某方法**的执行结果，只要第二次访问方法所带的参数与第一次**一致**，则直接返回第一次执行的结果

  可应用于性能提高



# custom-exception

**HOW**

- 创建Assert类抛出异常，需要传递String参数给Assert
- 自定义异常类继承RuntimeException，super()传递异常给RuntimeException
- 或者传递IErrorCode对象给Assert

# Dubbo

**process**

- 被调用服务和调用者服务都注册到Nacos，调用者使用Dubbo从Nacos获取服务并调用
- 调用远程的对象方法，像调用本地方法一样的简单

**bugs**

- paskage -> package

- 远程接口类和远程实现类的所在的包名**必须相同**

  ```yaml
  scan:
    base-packages: com.codeman.dubbo
  ```

  





# Gateway

**process**

- 客户端服务在Nacos进行注册，网关从Nacos获取客户端服务

  用户访问网关，即可访问客户端服务

**knowledge**

- bootstrap.yml使用于Spring Cloud的应用上下文，application.yml使用于Spring Boot的应用上下文

  bootstrap.yml先于application.yml加载



# JWT



# knowledge



# log



# M-S

> scalability可扩展性[ˌskeɪləˈbɪləti]、maintainability可维护性[meɪnˌteɪnəˈbɪləti]

## call-back

- 通过回调函数，使用一个方法就可
  - 调用n个类的相同方法，且顺便处理每一个属于不同实例的List资源
- 不通过回调函数，需使用两个方法
  - 1.大量重复书写相同方法名
  - 2.创建另一个方法来单独处理每一个List资源



## interface

- 要执行**某一分类的所有方法**

  可使用接口，统一执行该接口的**所有实例**即可

# monitor

> [ˈmɒnɪtə(r)]监控器

**bugs**

- localhoct -> localhost



# order-task

**HOW**

- 创建定时任务，每隔一段时间执行如下步骤
- 查询出订单超时时间
- 根据订单超时时间，查询出所有超时订单
- 根据超时订单，修改订单状态为关闭，而不是删除订单数据
- 根据超时订单，修改订单对应商品的锁定库存





# Redisson

**process**

- server-two没有休眠则输出、server-one休眠16s才输出

  而server-one率先获得锁，故server-two要等16s，在server-one输出后才输出



# returnR

**WHY**

- 对每次url调用返回的结果做统一处理，返回的R对象可以**显示在页面上**，起到给前端页面提示的作用

> 页面接收到的为：{"code":1,"data":{"id":1,"name":"Jone","age":18,"email":"test1@baomidou.com"},"message":"success"}

**HOW**

**返回R对象**return R.ok(userService.getId());

R.ok方法把数据库查询到的数据、成败信息整型表示、成败信息字符串封装成R对象，返回给前端

```
public static <T> R<T> ok(T data) { // R的两个位置泛型，确保了返回的R对象的泛型也是该泛型
    return restResult(CommonConstant.SUCCESS, data, "success");
}
```

**成败信息类**的类型

- 成败信息类为接口类型，因为接口的变量类型默认为public static final，代表该变量不可变且可访问

      public interface CommonConstant { //[ˈkɒnstənt]常量
      
          Integer SUCCESS = 1;
      
          Integer FAIL = 0;
      }

- 成败信息类为Enum类型

  ```
  public enum ResultCode {
      SUCCESS(1, "successful"),
      FAIL(0, "fail");
  
      private final long code;
      private final String message;
  
      ResultCode (long code, String message) {
          this.code = code;
          this.message = message;
      }
  
      public long getCode() {
          return this.code;
      }
  
      public String getMessage() {
          return this.message;
      }
  }
  ```

# RocketMQ



# template



# trigger-log

**HOW**

- 创建切面，其中连接点为添加@Log注解的所有方法，AOP通知的逻辑为操作数据库









# util







# xxl-job

**process**

- 在xxl-job任务调度中心界面，添加某任务，该任务的**标识符**与Controll层的@JobHandler("")**对应**

  界面上的点击执行，则调用后台的代码运行

**knowledge**

- 系统启动需要**搭建**xxl-job任务调度中心


