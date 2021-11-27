# 项目声明

- 该项目基建模块project_learning为Spring initializr工程，即SpringBoot工程，基建pom已引入springboot-web







# AutoCode_Redis

**funcion**：发送验证码

**HOW**：for循环联合Random生成验证码，保存在Redis里

**words**：

- auto code
- expire
- prefix











# problems

- 基建为maven项目，覆盖pom文件，导致没有Dependencies

  基建为springboot工程就可以















------



# projectFunctionPoint与mall、mallAns关系

> mall探索mall项目 -> mallAns记录探索过程中的knowledge -> 探索完成后进行projectFunctionPoint创造













# project规范

- ```
  import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
  import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
  import com.alibaba.csp.sentinel.dashboard.domain.Result;
  import com.alibaba.csp.sentinel.dashboard.repository.rule.InMemoryRuleRepositoryAdapter;
  import com.alibaba.csp.sentinel.util.StringUtil;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.web.bind.annotation.*;
  
  import java.util.Date;
  import java.util.List;
  import java.util.concurrent.CompletableFuture;
  import java.util.concurrent.ExecutionException;
  import java.util.concurrent.TimeUnit;
  ```

- // 注释

- 













# project english

constant
component
until
analyzer分析器 ['ænəlaɪzə] 













# projectFunctionPoint Thinking

**在projectFunctionPoint项目搭建一个新技术遇到这么多bugs，那以后做现目学习新的技术也会遇到很多的bugs，真的顶得住吗**



**什么让我项目编程变慢**

- 打字慢
- idea运行慢



**为什么会有bugs**

- 使用一个helper技术来辅助我们的系统
  - 在不完全了解该技术的时候，就很容易触发bugs
    - 要想使用该技术，就得接受自己不完全了解该技术所导致的触发bugs，要想不触发bugs也可以，不使用该技术，自己去搭建该技术类型的框架
    - 怎么在确定必须使用该技术，减少bugs的发生
      - 找hepler书写好的，你所需要的技术Demo帮忙













# **project_Debugs_experience**

- 出现bugs必须先记录bugs，在note上写好bugs解决思路
- bug全看完，可能后面有提示`Caused by`、`nexted Exception is`













# **project_coding_experience**

- 从server层的接口开始，接着是mapper层，再是控制层
- 提高打字速度与准确性，**精力都花在打字上忘记了接下来的思路**
- server层的每一次调用都事先写好sout调试













# 私信

**HOW**：

- **数据库查询更新**

  把消息发送给他人后，该消息的status字段默认为未读，而我们每次去查看消息列表会获取所有接受者为自己，未读的消

- **netty**











# 返回统一结果集

**作用：**对每次url调用返回的结果做统一处理，返回的R对象可以**显示在页面上**，起到给前端页面提示的作用

> 页面接收到的为：{"code":1,"data":{"id":1,"name":"Jone","age":18,"email":"test1@baomidou.com"},"message":"success"}



**HOW**：

**返回R对象**return R.ok(userService.getId());

R.ok方法把数据库查询到的数据、成败信息整型表示、成败信息字符串封装成R对象，返回给前端

```
public static <T> R<T> ok(T data) { // R的两个位置泛型，确保了返回的R对象的泛型也是该泛型
    return restResult(CommonConstant.SUCCESS, data, "success");
}
```

**成败信息类**的类型：

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













# 添加日志

**HOW**：

- 创建切面，其中连接点为添加@Log注解的所有方法，AOP通知的逻辑为操作数据库













# 发送验证码

**HOW**：for循环联合Random生成验证码，保存在Redis里



**bugs**

- ```
  java.lang.NullPointerException: null空指针异常
  ```

  sloved：private RedisService redisSerivice没有添加@Resource

  定位应该是对象，却测试变量，变量不可能空指针异常













# 登录

**HOW**：

1. 使用Spring Security对所有**页面**进行拦截，使用Spring Security默认的cookie-session验证授权

   problems：cookie和session怎么操作验证，还是不用自己操作

2. 在前端创建**前端拦截器**对所有**页面**进行拦截，登录后返回token保存在浏览器，登录后每一次的页面访问都携带token进行前端拦截器的验证授权

3. 使用Spring Security对所有**后端方法**进行拦截，登录后返回token保存在swagger-ui，登录后每一次后端方法访问都携带token进行Spring Security的验证授权

   problems：怎么在请求头添加token、怎么创建验证token的类













# 定时任务

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