> 你说下对MyBatis的理解？

如果没有MyBatis的支持，大家是怎么实现通过程序控制数据库的？首先我们需要为程序引入MySQL连接依赖`mysql-connector.jar`，再获得数据库注册驱动，接着创建数据库连接对象、SQL执行对象，把SQL语句发送到MySQL执行，最后关闭SQL执行对象和数据库连接对象。

整个过程是比较繁琐的，这是通过JDBC操作MySQL必走的过程。可实际开发可给不了你那么多时间，如果大家非要用JDBC去写大量的冗余代码也可以，能抗住催你开发进度的压力就行。

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

这是MyBatis编写SQL的写法。SQL的编写已经和程序运行分离开，消除了大量JDBC冗余代码，同时MyBatis还能和Spring框架集成。整个SQL编写的流程变得更加灵活也更加规范化。

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

> 