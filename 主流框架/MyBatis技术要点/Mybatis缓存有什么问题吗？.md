## 1. Mybatis概要

### 1.1 Mybatis理解

如果没有MyBatis的支持，大家是怎么实现通过程序控制数据库的？首先我们需要为程序引入MySQL连接依赖`mysql-connector.jar`，加载数据库JDBC驱动，接着创建数据库连接对象`Connection`、SQL语句执行器`Statement`，再把SQL语句发送到MySQL执行，最后关闭SQL语句执行器和数据库连接对象。

整个过程是比较**繁琐**的，这是通过JDBC操作MySQL必走的过程。可实际开发可给不了你那么多时间，如果大家非要用JDBC去写大量的冗余代码也可以，能抗住催你开发进度的压力就行。

这是JDBC操作的过程。

```java
public class JDBCController {
    private static final String db_url = "jdbc:mysql://localhost:3306/db_user";
    private static final String user = "root";
    private static final String password = "root";
    
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        String sql = "select * from user order by id desc";
        try {
            connection = DriverManager.getConnection(db_url, user, password);
            statement = connection.createStatement();
            int result = statement.executeUpdate(sql);
            System.out.println(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
```

MyBatis能帮助我们什么？早在2002年，MyBatis的前身iBatis诞生，并于2010年改名为MyBatis。该框架引入了**SQL映射**作为持久层开发的一种方法，也就是说我们不需要把SQL耦合在代码里，只需要把SQL语句单独写在XML配置文件中。

以下是MyBatis编写SQL的写法。SQL的编写已经和程序运行分离开，消除了大量JDBC冗余代码，同时MyBatis还能和Spring框架集成。整个SQL编写的流程变得更加灵活也更加**规范化**。

```java
@Mapper
public interface UserMapper extends BatchMapper<UserDO> {
    List<UserDO> selectAllUser();
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.JavaGetOffer.UserDO">

    <select id="selectAllUser" resultType="org.JavaGetOffer.UserDO">
        select * from user order by id desc
    </select>
    
</mapper>
```

### 1.2 SqlSession是什么

从我们偷偷访问某个小网站开始，到我们不耐烦地关闭浏览器或者退出登录时，我们作为用户和网站的一次会话就结束了。MyBaits框架要访问数据库同样要与数据库建立通信桥梁，而SqlSession对象表示的就是MyBaits框架与数据库建立的**会话**。

我们可以利用SqlSession来操作数据库，如下代码。

```java
    @Test
    public void testMybatis() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<UserDO> userList = userMapper.listAllUser();
        System.out.println(JSON.toJSONString(userList));
    }
```

## 2. Mybatis缓存

### 2.1 Mybatis缓存分类

软件系统合理使用缓存有一个好处。有了缓存，在原始数据没有更新的情况下，我们不需要重新再去获取一遍数据，这也减少了数据库IO，达到提升数据库性能的目的。

MyBatis同样提供了两个级别的缓存，一级缓存是基于上文提到的SqlSession实现，二级缓存是基于Mapper实现。

一级缓存作用在同一个SqlSession对象中，当SqlSession对象失效则一级缓存也跟着失效。我们梳理下一级缓存的**生命周期**。首先第一次查询时会把查询结果写入SqlSession缓存，如果第二次查询时原始数据没有改变则会读取缓存，但如果是修改、删除、添加语句的执行，那SqlSession缓存会被全部清空掉，这也是为了防止**脏读**的出现。

一级缓存缓存底层使用的是一个简单的Map数据结构来存储缓存，其中key为`SQL + 参数`、val为`查询结果集`。一级缓存的生命周期如下。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/7ffbd58ab80d460a877dd0cba43de01b.png#pic_center)


二级缓存的作用域是同一个命名空间**namespace**的Mapper对象，也就是说同一个Mapper下的多个SqlSession是可以共用二级缓存的。二级缓存的缓存写入、清空流程和一级缓存相似，但二级缓存的生命周期是和**应用程序的生命周期**一致的。为什么？因为Mybatis框架与Spring IOC集成的Mapper对象是单例对象。

另外大家还需要注意下，Mybatis的一级缓存是默认开启的且**不能关闭**，而二级缓存则需要我们手动开启，我们需要在配置文件中配置`cacheEnabled`参数。

```xml
<configuration>
  <settings>
    <setting name="cacheEnabled" value="true"/>
  </settings>
```

### 2.2 Mybatis缓存局限性

缓存是好，就是问题有点多，目前大厂大都禁止了Mybatis缓存的使用。

南哥总结了下，主要有以下原因。

（1）适用场景少

Mybatis二级缓存更适用于读多写少的业务场景，但是对于**细粒度**的缓存支持并不友好。举个用烂了的商城例子，每个商品信息的更新是非常**频繁**的的，而让用户每次都看到的是最新的商品信息又非常重要。

在同一个namespace的Mapper中一般会包含多个商品信息的二级缓存，只要有某一个商品信息更新了，则所有商品缓存都会全部失效。那其实在这个业务场景中，二级缓存的存在已经没有多大必要了，还反而增加了系统复杂性。

（2）数据不一致性问题

如果多个不同namespace的Mapper都共同操作同一个数据库表的情况下，第一个Mapper更新了数据库表会清空它本身的二级缓存，但其他namespace的Mapper是没有感知的，仍然缓存的是旧数据，数据不一致的问题就出现了。

（3）不适用于分布式系统

现在还用单机部署的业务已经不多了，大家都紧跟潮流搭了个分布式、高可用的系统。在分布式系统中，如果每个节点都使用自己的本地缓存，假如现在节点A更新了缓存，但节点B、节点C是不会进行同步更新的，同样产生了数据不一致的问题。

## 3. Mybatis分页插件

Mybatis分页的原理其实很简单，没有想象的那么复杂。我们只需要拦截SQL查询语句，再把SQL语句作为子查询，外面包裹一层`SELECT * FROM`后再加上`LIMIT`的分页约束语句。

如下SQL示例，确实挺简单的。

```sql
SELECT * FROM user
SELECT u.* FROM (SELECT * FROM user) u LIMIT M, N
```