# structure

```lua
universe
├── autoCode-Redis -- 验证码
├── bean-mapping -- bean映射
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
├── log4j
├── monitor -- 监视器
     ├── springboot-client -- 被监视者
     └── springboot-server -- Spring Boot Admin服务端
├── order-task -- 定时任务
├── Redisson -- 分布式锁
     ├── server-one -- 旧版本
     └── server-two
├── returnR -- 统一结果集
├── RocketMQ
├── template -- 项目模板
├── trigger-log -- 日志触发
├── util -- 生成器
└── z-dp
```





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





# monitor

> [ˈmɒnɪtə(r)]监控器

**bugs**

- localhoct -> localhost





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



# custom-exception

**HOW**

- 创建Assert类抛出异常，需要传递String参数给Assert
- 自定义异常类继承RuntimeException，super()传递异常给RuntimeException
- 或者传递IErrorCode对象给Assert










# trigger-log

**HOW**

- 创建切面，其中连接点为添加@Log注解的所有方法，AOP通知的逻辑为操作数据库





# autoCode-Redis

**HOW**

- for循环联合Random生成验证码，保存在Redis里

**bugs**

- ```
  java.lang.NullPointerException: null空指针异常
  ```

  sloved：private RedisService redisSerivice没有添加@Resource

  定位应该是对象，却测试变量，变量不可能空指针异常











# order-task

**HOW**

- 创建定时任务，每隔一段时间执行如下步骤
- 查询出订单超时时间
- 根据订单超时时间，查询出所有超时订单
- 根据超时订单，修改订单状态为关闭，而不是删除订单数据
- 根据超时订单，修改订单对应商品的锁定库存











