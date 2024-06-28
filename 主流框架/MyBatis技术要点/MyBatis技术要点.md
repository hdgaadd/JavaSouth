大家好，我是南哥。

一个对Java程序员进阶成长颇有研究的人，今天我们接着新的一篇Java进阶指南。

为啥都戏称后端是CRUD Boy？难道就因为天天怼着数据库CRUD吗？要我说，是这个岗位的位置要的就是你CRUD，你不得不CRUD。哪有公司天天能给你搭建高并发、高可用、大数据框架的活呢，一条业务线总要成长吧，慢慢成熟了就要装修工来缝缝补补、美化美化，也就是CRUD的活。

不能妄自菲薄CRUD Boy，我们是后端工程师。今天来指南下操作数据库之MyBatis框架。

> 你说下对MyBatis的理解？

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

> 那SqlSession知道吧？

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

> Mybatis的缓存有哪几种？

软件系统合理使用缓存有一个好处。有了缓存，在原始数据没有更新的情况下，我们不需要重新再去获取一遍数据，这也减少了数据库IO达到提升数据库性能的目的。

MyBatis同样提供了两个级别的缓存，一级缓存是基于上文提到的SqlSession实现，二级缓存是基于Mapper实现。

一级缓存作用在同一个SqlSession对象中，当SqlSession对象失效则一级缓存也跟着失效。我们梳理下一级缓存的**生命周期**。首先第一次查询时会把查询结果写入SqlSession缓存，如果第二次查询时原始数据没有改变则会读取缓存，但如果是修改、删除、添加语句的执行，那SqlSession缓存会被全部清空掉，这也是为了防止**脏读**的出现。

![绘图](D:\code\z-mine\JavaGetOffer\主流框架\MyBatis\绘图.png)

二级缓存的作用域是同一个命名空间namespace的Mapper对象，也就是说同一个Mapper下的多个SqlSession是可以共用二级缓存的。二级缓存的缓存写入、清空流程和一级缓存相似，但二级缓存的生命周期是和**应用程序的生命周期**一致的。为什么？因为Mybatis框架与Spring IOC集成的Mapper对象是单例对象。

另外大家还需要注意下，Mybatis的一级缓存是默认开启的且**不能关闭**，而二级缓存需要我们手动开启。

```xml
<configuration>
  <settings>
    <setting name="cacheEnabled" value="true"/>
  </settings>
```

> Mybatis缓存有什么问题吗？

缓存是好，就是问题有点多，主流Java大厂目前都禁止了Mybatis缓存的使用，大家在（）也可以看到。

南哥总结了下，主要有以下原因。

（1）适用场景少

Mybatis二级缓存更适用于读多写少的业务场景，但是对于**细粒度**的缓存支持并不友好。举个用烂了的商城例子，每个商品信息的更新是非常频繁的的，而让用户每次都看到的是最新的商品信息又非常重要。

在一个Mapper对象中一般会包含多个商品的二级缓存，如果某一个商品信息更新了则所有商品缓存会全部失效。那其实在这个业务场景中，二级缓存的存在已经没有多大必要了，还反而增加了系统复杂性。

（2）不适用于分布式系统

现在还用单机部署的业务已经不多了，大家都紧跟潮流搭了个分布式、高可用的系统。在分布式系统中，每个节点都使用自己的本地缓存的话，假如现在节点A更新了缓存，但节点B、节点C没有同步更新，这就产生了数据不一致的问题了。

在分布式环境下，我们将MyBatis的localCacheScope属性设置为STATEMENT就可以让Mybatis每次只要执行查询就会清空缓存。