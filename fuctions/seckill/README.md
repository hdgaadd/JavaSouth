# choosing technology

| dependency    | version |
| :------------ | ------- |
| JDK           | 8.0     |
| RocketMQ      | 4.9.1   |
| Redis         | 5.0.14  |
| Sentinel      | 1.8.0   |
| SpringBoot    | 2.5.2   |
| Mybatis-Plus  | 3.4.1   |
| apache-jmeter | 5.4.1   |
| apache-maven  | 3.2.5   |

# 

# database design

**table design**

- seckill_activity

  > total_stock：真实库存
  >
  > id、name使用BTree

- commodity

  > commodity[kəˈmɒdəti] 商品
  >
  > id使用BTree

- order

  > amount：订单金额
  >
  > id使用BTree
  >
  > 0是无效、1为正常、2为支付、3为超时

- user

  > id使用BTree

**UML**

 ![seckill](https://codeman666.oss-cn-beijing.aliyuncs.com/GitHub/seckill/seckill.png)

**table update**

- seckill_order的code，类型改为varchar(100)





# global design

**超卖现象分析**

- 数据库更新库存需要获取数据、更新数据、写入数据，所消耗时间远比操作一个在代码层面上的static变量要多得多

  无法确保高并发**秒杀**环境下，**每次请求**获取到的库存都是**最新库存**

**实现高并发组件**

- Redis存储库存变量，一次秒杀请求执行具有原子性Lua脚本，让库存同步-1

  确保高并发场景，秒杀不会出现**超卖现象**

- 把获取库存、更新库存设置在**同一个方法**内，且该方法加**分布式锁**

  确保高并发场景，秒杀不会出现**超卖现象**

- **创建、支付订单**会更新数据库**库存**，把该任务交给RocketMQ

  消息队列的任务具有先进先出特点，实现高并发**创建、支付订单**，任务**依次执行**，不会出现库存数据不一致

- 为什么RocketMQ不处理超卖问题

  RocketMQ需要在**代码层面**上，保存库存变量，不现实

**谁真正改变库存**

- Redis的Lua脚本只是确保秒杀的时候，不会出现**超卖**问题，**没有真正改变**数据库库存
- 秒杀成功的请求会继续创建订单、支付订单才会真正**改变**数据库库存

**系统流程**

- 管理员创建活动，添加活动某一定数量商品，该商品的库存**自动减去**该数量
- 用户根据**活动id**秒杀订单，Redis里的库存秒杀**完**，其他秒杀请求被**拒绝**
- 秒杀成功的请求**继续创建**订单，更新数据库库存，将用户加入**限选用户**
- 用户在规定时间内**支付**订单
- 规定时间后判断用户**是否支付**，未支付的更新数据库库存，Redis的**库存+1**，用户从限选用户**取出**
- Redis库存+1，其他用户可以**继续秒杀**





# system problems

- 添加用户为限选状态后再创建订单，但是创建订单失败，此时RocketMQ检查不到该订单无法将该用户从限选状态解除

  已将创建订单与添加限选用户顺序调换

# content-one

**有限库存，使用Redis+Lus脚本防止超卖问题**

- 根据activity_id扣减活动表的库存

  根据id查询活动表，查询库存，如果可用则-1

- 创建Jedis配置类实例Jedis连接池，引入application.yml的paramter设置进Jedis连接池实例

  创建Redis配置类，编写方法get、set操作Redis、在Redis抢购的方法

  创建Redis启动类，让启动SpringBoot即把库存放入Redis

  把库存放在Redis，在Redis里面进行抢购

  - Lua脚本的逻辑是判断库存，扣减库存	`[lu er]`

    Lua脚本类似**Redis的事务**，可以确保Lua脚本执行的程序是**原子性的**，故可以利用Redis处理数据库超卖问题

**knowledge**

- 抢购成功远超商品可用库存，即是超卖

**problems**

- 浏览器默认是使用GET提交，后端是Post的话，会报错405

- the lecturer[ˈlektʃərə(r)]不是一定要库存全部清0，而是要展示库存就几个，却有好几十个purchase succeed

- update未完成，就有请求去get数据，导致get到的数据是未-1的数据

  把get请求设置在update**完成之后**

# content-two

**使用消息队列创建订单**

- 实例rocketMQ，测试发送消息
- 传入活动id和和用户id秒杀，秒杀成功**则**创建订单
- 将两id取出活动表，封装成订单对象
- **消息队列**：将订单通过RocketMQ发送，接收者接收到消息把活动的限定库存-1，如果库存-1成功修改order状态为1
- 把该活动id+用户id作为key，加入到Jedis连接池里，作为**限选用户**
- 插入订单到数据库

**查询订单**

- 根据order编号查询活动

**支付订单**

- 传入订单编号
- 查询订单，如果status为0则失效，如果为null则返回
- 暂定支付成功，将status为2，set支付时间，更新订单，将订单发送给**RocketMQ**
- 接收order对象消息，根据order的活动id将库存-1

**knowledge**

- 无法把mq作为另一个服务，不启动也行，因为秒杀即代表创建订单，两者不可分割，则使用微服务架构还得再启动另一个服务，麻烦



# content-three

**检查订单是否支付**

- 创建订单后，再发送延迟消息，
- 先根据先前的订单id，获取此时最新的该订单，或许该订单已经支付更新状态了
- 判断订单的status是否为2，即支付成功，是的话放回
- 否的话更新数据库库存，更新Redis库存，将用户从锁定状态解除

**限选用户设计**

- 创建订单，把用户设置为限选用户
- 每次秒杀查询用户是否为限选用户
- 订单超时，将用户从限选用户中移除

**problems**

- **基本数据类型不能和null比较**

  ```
  public class test {
      public static void main(String[] args) {
          Integer status = null;
          if (status != 2) {
              System.out.println("sfj");
          }
      }
  }
  Exception in thread "main" java.lang.NullPointerException
  ```

- 对Mybatis-Plus的update(user, queryWapper)**没把握**，这个应是更新所有

  ```
  // 根据订单编号查询订单
  QueryWrapper<SeckillOrder> seckillOrderQueryWrapper = new QueryWrapper<>();
  seckillOrderQueryWrapper.eq("code", orderCode);
  SeckillOrder seckillOrder = seckillOrderMapper.selectOne(seckillOrderQueryWrapper);
  // 更新订单
  seckillOrderMapper.update(seckillOrder， null);
  ```

  而updateById(user)是根据user的id更新

  ```
  // 根据订单编号查询订单
  QueryWrapper<SeckillOrder> seckillOrderQueryWrapper = new QueryWrapper<>();
  seckillOrderQueryWrapper.eq("code", orderCode);
  SeckillOrder seckillOrder = seckillOrderMapper.selectOne(seckillOrderQueryWrapper);
  // 更新订单
  seckillOrderMapper.updateById(seckillOrder);
  ```

- availableStock是**支付订单**才会操作，订单超时**不用操作**availableStock

- 雪花创建的订单编号会**相同**，因为用户id都一样，已再编号的基础上添加Random

- 添加限选用户是是sadd，而不是set

- rocketmq会接受之前的消息



# content-four

- 根据活动id获取秒杀活动和秒杀商品时，先从Redis获取，为空获取后填充到Redis
- 创建后端方法，可以让前端循环获取活动的倒计时



# content-five

- 秒杀商品时，使用Sentinel进行限流，设置该Controller方法每秒可以通过的请求数






