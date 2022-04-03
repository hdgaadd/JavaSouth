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
├── repeat-submit-intercept -- 防重复提交解决方案
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

## automicReference

- Automic家族保证多线程环境下数据的原子性，相比synchronized更加轻量级，该类操作的是引用类型

## optional

- JDK8为了解决**NPE**问题，参考Google类库中的Optional类设计所创建的工具类

## stringJoiner

- 创造一个字符序列，可添加前缀、后缀、分隔符





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

**bugs**

1. ```
   You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'order LIMIT 0, 1000' at line 1
   ```

   - 表名不能为order，否则SQL语句失败

2. `Invalid bound statement (not found)com.condemn.mapper.UserMapper.test` `[ɪnˈvælɪd]`无效的绑定语句(未找到)

   - mapper层的xml没有书写属性ResultType

     SQL返回List对象resultType应该书写什么

   - 或许是.xml的namespace写错，.xml的id与mapper方法名不一致、启动类注解设置mapper路径错、application.yml设置mapper路径错

   - **找书写了xml且能运行的mybatis-plusDemo**

     **sloved**：上面的解决思路是正确方向，参考Demo后发现必须在pom.xml的`<build>`添加mybatis的一些关于xml的配置，且application.yml也必须指定mapper路径

   ------

   代码生成器bugs，🙄🤬

3. `java.lang.IllegalStateException: Failed to load ApplicationContext`

   - codeman修改为其他name就可以，那为啥其他不修改也可以
     - 可能是之前误触了修改全部模块的name为其他name，**补充**：误触了修改name后触发了代码生成器的bugs，com.codeman为一个文件夹
   - application.yml的`mapper-locations: classpath*:com/code/**/xml/*Mapper.xml`的改为codeman就可以，sb
     - 可能是之前误触了修改全部模块的name为其他name

4. 把codeman改为code是全局改变的

   - **sloved**：没有看IDEA提升，有可以修改全局的，也有可以只修改本模块的 

5. java.lang.IllegalStateException: Unable to find a @SpringBootConfiguration, you need to use @ContextConfiguration or @Spri

   - **sloved**：测试类和启动类的包路径不一致

     - bugs3、4、5的出现都是由于未知道pom.xml的`<build>`添加mybatis的一些关于xml的配置，把所有模块的name从codeman改为了condemn            

       **该解释错误，bugs3、4、5的出现是由于触发代码生成器的bugs**

6. 创建了一个新的test模块，又出现`Invalid bound statement (not found): com.codeman.mapper.UserMapper.test`

   - 可能是复制代码生成器代码后复制沾沾的原因，且autocode_redis、relax不是使用代码生成器代码配置了mybatis的mapper路径后都不会报该类错误，只有使用代码生成器的模块会报错，更证明了是代码生成器的问题

     **sloved：**文件夹com.codeman连为一个文件夹，而不是两个文件夹

     - 在`推荐商品`中测试类还是报错`java.lang.IllegalStateException: Failed to load ApplicationContext`😆，

       **sloved**：但仔细看报错下面的提示`Caused by: java.lang.IllegalArgumentException: Could not find class [org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties]`，故其实并不是代码生成器错误，而是ElasticsearchProperties获取不到而导致测试类异常

       删除有关Elasticsearch的，即不会报错





# Redisson

**process**

- server-two没有休眠则输出、server-one休眠16s才输出

  而server-one率先获得锁，故server-two要等16s，在server-one输出后才输出



# repeat-submit-intercept

> 重复提交，指的是本次url方法**未执行完成**，对口该url方法提交重复的数据

**process**

- 每次提交，获取**Redis分布式锁**

  - 若获取锁成功，则执行提交

    后**解除**该锁，可进行**下次提交**

  - 若获取锁失败，则表示本次提交**正在进行中**，防止了重复提交

    **本次**提交后，才可以进行**下一次**提交

**knowledge**

- getServletPath()获取的是**访问**的url路径

- 若多个浏览器**窗口**执行**相同**url，浏览器会自动等待第一个窗口url请求后，再执行下一个窗口的相同url

  故出现，多个窗口测试本module，**不会出现**重复提交报警

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









# ShardingSphere

**process**

- 采用**单库分表** + 主从复制

  单库分表进行**写**操作，其他库进行读操作

  之所以单库写，是因为分库写的场景**不多见**

- 主从复制交给**MySQL自己进行**

  在MySQL创建多个不同端口的服务器

**knowledge**

- 配置**分片策略**，是把数据分配到多个表中

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





# 私信

**HOW**

- **数据库查询更新**

  把消息发送给他人后，该消息的status字段默认为未读，而我们每次去查看消息列表会获取所有接受者为自己，未读的消

- **netty**

# 登录

**HOW**

1. 使用Spring Security对所有**url页面**进行拦截，使用Spring Security默认的**cookie-session**验证授权

   我们**需要**在前端创建**前端拦截器**设置登录后每一次的url请求都携带**cookie**

   **problems**

   - cookie和session怎么操作验证，还是不用自己操作

     Spring Security自动帮我们管理cookie-session

     - 因为实例化Spring Security后使用后端的url就默认要输入账号密码

     - 在chrome登录后关闭浏览器，**第二次登录还是不用登录**，说明cookie保存在了brower了

       继续实验，在Edge登录，得输入账号密码

2. 使用Spring Security对所有**url方法**进行拦截，使用JWT的**token**验证授权

   我们**需要**在前端创建**前端拦截器**设置登录后每一次的url请求都携带**token**，在后端创建**Spring Security过滤器**，获取请求头**的token并验证，成功则**调用**Spring Security的success()

   



**knowledge**

- **谁需要token**

  - 前台的页面不用token都可以访问，token只是为了获取用户信息填充到**页面上沿**，或订单支付携带token给后端获取用户信息

  - 后台所有页面的访问都会**触发**后端方法，而后端的所有方法都需要**获取token**，没有token则**返回401**，前端接收到401返回码则**跳转**到login页面

- **token操作**

  - 在requrest.js添加以下代码，使前台在每次**页面**请求或后端**url请求**，都**添加token**

    ```
    // 创建拦截器  http request 拦截器
    service.interceptors.request.use(
      config => {
      // debugger
      // 判断cookie里面是否有名称是guli_token数据
      if (cookie.get('guli_token')) {
        //把获取cookie值放到header里面
        config.headers['token'] = cookie.get('guli_token');
      }
        return config
      },
      err => {
      return Promise.reject(err);
    })
    ```

  - **获取**请求中的**token**

    - guli

      **后端**接收的参数多了HttpServletRequest 

      ```
      public ResultEntity generateOrder(@PathVariable String courseId, HttpServletRequest request)
      ```

    - 其他

      在public class SecurityConfig extends WebSecurityConfigurerAdapter类，设置自定义**Spring Security过滤器**，拦截所有的url获取token

  - 后台**拦截**所有页面的代码

    - guli的是由**vue-admin**提供的🙄

      vue-admin里**自动生成**vue_admin_template_to的token

    - 访问前端的所有页面都会**触发**后端方法，而后端的所有方法**都需要获取token**，没有token则**返回401**，前端接收到401返回码则**跳转**到login页面，即实现了**拦截**所有页面

- **UserDeatils有什么用**

  - 保存用户基本信息与权限信息

- **Spring Security的作用**

  - cookie-session：拦截url、自动发送cookie与验证cookie
  - JWT：拦截url

- **UserDetails里的List<权限>有什么用**

  把权限封装在token里面

  - 那`@PreAuthorize("hasAuthority('pms:brand:read')")`是否和UserDetails里的List<权限>相关

    - 这个判断权限应该也是得自己实现：获取方法的注解属性，判断token里的权限是满足注解属性

      而UserDetails里的List<权限>作用是封装在token里面



**步骤**

- 创建拦截url的**拦截器类**，如果token成功就调用Spring Security的**成功方法**

- 修改Swagger的配置类，设置添加添加token按钮后，每次url访问都**携带token**

- 创建Spring Security配置类 ,设置**取消**cookie-session来**验证**，设置根据用户name返回UserDetails类

  - name是怎么传给Spring Security的

    loadUserByUsername(name)是调用Spring Security的方法，故是Spring Security帮我们**返回UserDetails**

    那我们创建Spring Security配置类**实现**Spring Security的**该方法**即可

    ```
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    
    
    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
    	throw new BadCredentialsException("密码不正确");
    }
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    
    ```





**注册步骤**

- 查询是否用户名已存在
- 对用户的密码进行加密后，保存用户

**登录步骤**

- 用户登录传输账号秘密
- 根据用户传输的账号，查询出把用户实体类、角色集合类封装到Spring Security的UserDetails类
- 如果密码与UserDetail类的密码不一致，则返回error
- 登录成功后
  - 添加登录记录
  - 把token和token头部，使用HashMap存储后返回



**problems**

- resultMap写欠domain

  ```
  resultType="com.codeman.Admin"
  ```

- `A component required a bean of type 'org.springframework.security.crypto.password.PasswordEncoder' that could not be found.`

  - 应该是Spring Security的某些配置类没有添加上

    没添加一个创建PasswordEncoder的配置类 

- `Data truncation: Data too long for column 'password' at row 1`’

  password字段如今设置为500

- 怎么把Spring Security的验证方法设置为我们的方法

  创建拦截url的拦截器类，如果token成功就调用**Spring Security的成功方法**

- 怎么在请求头添加token、怎么创建验证token的类

  修改Swagger的配置类，设置添加添加token按钮后，每次url访问都携带token







# 推荐商品

**HOW**

- 服务器启动在MySQL查询出所有商品，保存在Elasticsearch
- 用户根据该关键字在Elasticsearch进行搜索，同时把所有数据根据是否促销、销量、库存等先后进行排序，根据这三个排序可以把这三个属性都没优势的商品排在最后
- 排序好的与该关键词相关的所有数据，即可带给用户更满意的搜索结果



**knowledge**

- SKUStock Keeping Unit库存单位，SPUStandardKeeping Unit产品标准单位



**problems**

- 把所有数据从数据库搜索出来后保存在Elasticsearch，还是每次触发url后才从数据库搜索后保存在Elasticsearch

  - 如果按前者，那还不如让Elasticsearch当数据库

    仅仅只是商品表保存在Elasticsearch

  - 每次更新数据，Elasticsearch是不能获取到的，故Elasticsearch应该保存不变的商品

    - 应该是不变，且查询量大的数据保存在Elasticsearch

    - 全部从从数据库查询后保存在Elasticsearch，再在Elasticsearch进行排序搜索，Elasticsearch在大量数据的关键字搜索与排序应该是比数据库更加有优势

      更新数据库同时要更新elasticsearch，更新操作远远小于查询，故同时要更新Elasticsearch造成的开销可以忽略



**bugs**

- 以为是EsProduct的type = "product"没写，没仔细看报错提示的”S"对应的东西`<S extends T> S save(S entity);`

  传递的是List集合，但S只是一个普通对象

  Inferred type 'S' for type parameter 'S' is not within its bound; should extend 'com.codeman.domain.EsProduct'

- `Error creating bean with name 'esProductController': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'productRepository': Invocation of init method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.data.elasticsearch.repository.support.NumberKeyedRepository]: Constructor threw exception; nested exception is org.springframework.data.elasticsearch.ElasticsearchException: Failed to build mapping for product:product`

  `nested Exception is` 嵌套异常是，故异常类为is后

- 把测试类改为com.codem，但报错Could not load class with name: com.codeman.RelaxApplicationTests

  - 是否修改了文件夹，而没有项目内核没修改

    - 怎么修改项目内核

      - 没有所谓的项目文件夹内核，只有directory，rename是持久性的

- Caused by: java.lang.IllegalArgumentException: Could not find class [org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties]

  - 报错原因为pom文件引入的elasticsearch无法实例ElasticsearchProperties，应该是本module的elasticsearch包与springboot其他包冲突
    - 把模块复制后脱离project_learning模块，还是报错
      - 打算参考另一个helper的Demo





