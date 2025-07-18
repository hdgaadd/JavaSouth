# MySQL面试必问：MySQL事务四大特性、事务隔离级别

## 1. 事务的特性

MySQL事务有四大特性。

1. 原子性(atomicity)：一个事务必须是一个不可分割的**最小工作单元**，整个事务所有的操作，要么成功提交，要么都失败回滚。
2. 一致性(consistency)：事务总是从一个一致性状态转换为另一个一致性状态。
3. 隔离性(isolation)：一个事务所作出的修改在还没有提交之前，对其他事务来说是不可见的。
4. 持久性(durability)：如果事务进行提交后，其所做的修改必须是永久性的，不会因为系统崩溃而丢失修改。

## 2. 事务隔离级别

SQL标准定义了四种隔离级别，较低级别的隔离通常来说系统开销更低些。

1. READ UNCOMMITTED（未提交读）：事务的修改，即使没有提交，对其他事务来说也是可见的。这是最低级别的事务隔离，企业生产中很少使用到。
2. READ COMMITTED（提交读）：事务在未提交前，所做的修改对其他事务是不可见的。这个隔离级别也称为不可重复读，主要是因为两次重复的数据读取，可能会产生两种完全不同的结果。
3. REPEATABLE READ（可重复读）：这个事务隔离级别保证了一个事务多次读取都是同样的结果，能够解决前面两个隔离级别可能产生的不可重复读问题。另外可重复读是`MySQL`默认的事务隔离级别。
4. SERIALIZABLE（可串行化）：该隔离级别会**强制事务串行执行**，同时对读取的每一行数据都加上锁，来。通过这种方式可以解决**幻读**的事务问题，不过可能导致锁竞争问题和大量的`SQL`超时。

### 2.1 幻读

并发事务带来的问题主要有四种，可以用上面我们谈到的事务隔离级别来处理。

1. 脏读：一个事务读取到另一个事务未提交的数据。

2. 不可重复读：一个事务多次读取同一数据，另一个事务**修改了**该数据，导致第一个事务第二次读取数据发现和第一次读取的**数据不一致**。

3. 幻读：一个事务多次读取同一数据，另一个事务给这些数据**插入删除了某些内容**，导致第一个事务数据的**数量**发生改变。

4. 丢失修改：一个事务修改了某个数据，另一个事务与其读取同一数据且原始值都相同，另一个事务修改数据后提交，导致第一个事务的修改操作丢失。

### 2.2 处理幻读问题

幻读可以采用我提到的`SERIALIZABLE`（可串行化）隔离级别来解决幻读，事务按顺序执行，也就不会有幻读问题。

MySQL也提供了其他方法来处理幻读问题。

1. 设置间隙锁，在两个索引值之间的数据进行加锁，可以杜绝其他事务在这个范围内对数据数量的影响。

2. next-key锁就是间隙锁和行锁的**组合**，通过间隙锁锁住**区间值**、行锁锁住**行本身**。

### 2.3 死锁问题

死锁是因为多个事务互相占用对方请求的资源导致的现象，要打破这个问题需要回滚其中一个事务，这样另一个事务就能获得请求资源了，而回滚的事务只需要重新执行即可。

InnoDB引擎目前处理死锁的方法是通过持有行级排他锁的数量来判断，**持有最少行级排他锁**的事务会进行回滚。

### 2.4 隔离级别相关命令

MySQL默认隔离级别是可重复读，企业生产一般也是用的这个隔离级别。

查看隔离级别的指令：

```sql
select @@tx_isolation
```

设置隔离级别为可重复读的指令：

```sql
set session transaction isolation level repeatable read
```
# MySQL面试必问：SQL如何优化、索引如何设计

## 1. 慢查询

> ***面试官：知道MySQL慢查询吗？***

MySQL的慢查询日志可以记录**执行时间超过阈值**的SQL查询语句，所以我们可以利用该日志查找出哪些SQL语句执行效率差，从而对SQL语句进行优化。

MySQL5.7以上版本可以通过SET命令来开启慢查询日志。

```mysql
     SET GLOBAL slow_query_log=ON;
     SET GLOBAL long_query_time=2;
     SET SESSION long_query_time=2;
```

开启完慢查询日志，我们找到该日志的位置，打开文件即可查询慢查询的SQL。

```mysql
     ＃　查询慢查询日志文件位置
     SHOW VARIABLES LIKE '%slow_query_log_file%';
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/a44fc1d0c2d7d6077689563284f8ccf1.png)


打开DESKTOP-ALU4BOC-slow.log文件，找到慢查询SQL为：`select sleep(11)`。

```xml
D:\MySQL\bin\mysqld, Version: 5.5.40 (MySQL Community Server (GPL)). started with:
TCP Port: 3306, Named Pipe: MySQL
Time                 Id Command    Argument
# Time: 220828 21:40:28
# User@Host: root[root] @ localhost [127.0.0.1]
# Query_time: 11.004454  Lock_time: 0.000000 Rows_sent: 1  Rows_examined: 0
use mysql;
SET timestamp=1661694028;
select sleep(11);
```

## 2. SQL优化

### 2.1 表设计优化

> ***面试官：在工作中你怎么优化SQL的？***

业务开发中涉及数据库的第一步是表设计，要优化SQL就要从第一步开始做起。

MySQL表设计要尽可能满足数据库三大范式，帮助大家回顾下：

1. 第一范式：数据库表中的每一列都是不可再分的属性，属性相近或相同的列应该**合并**。

2. 第二范式：满足第一范式的条件下，一个表只能描述一个对象。如果某些列经常出现数据重复，应该把这些列作为**另一个表**。

3. 第三范式：满足第二范式的条件下，表中的每一列都只能依赖于**主键**，即直接与主键相关。

我们在业务开发中遇到**反第二范式**的情况是最多的，例如以下订单明细表的设计，每一个订单明细都包含了**重复**的商品名称、商品单位、商品价格，这三个字段属于字段冗余存储。如果表的数据量级很大，那造成的冗余存储量是可想而知的，而且最要命的问题是如果要修改某一个商品名称，那所有的订单明细数据**都要修改**。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/248af310b00226526ad386b104b61d29.png)

我们可以遵循第三范式，把冗余的字段抽出一个新的商品表，当要查询订单明细时只需要把两表通过商品id进行连接即可。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/12daddf87ec5da36859fea06d658314b.png)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/6b8d792d3395ebe87e4886ec1a927ad5.png)

> ***面试官：遵循第二范式就一定最优？***

遵循第二范式的表设计不一定是最优的情况，还是那句话，要根据实际的业务场景权衡利弊。

虽然把冗余数据抽离出去了，但却增加了表的数量，也意味着查询数据时表之间的**join连接操作**也会变多。而join连接的性能是比较低的，有可能join操作会成为数据库性能的瓶颈。

### 2.2 SQL语句优化

> ***面试官：还有呢？***

SQL优化除了做好表设计的优化工作，还需要对SQL语句进行优化。而SQL查询语句的优化主要从**覆盖索引**、**避免索引失效**、**减少不必要的查询**三个方面入手。

一、从覆盖索引的角度。

**order by排序**的字段要尽量覆盖索引。如果使用**非索引字段**进行排序，MySQL会进行额外的**文件排序**，将查询结果根据非索引列在**磁盘**中再排序一次。当我们使用explain关键字分析SQL时会发现Extra会出现`Using filesort`。

**group by分组**要尽量覆盖索引。如果使用**非索引字段**进行分组，MySQL只能进行全表扫描后建立临时表才能得出分组结果。

另外我们可以使用explain关键字来分析SQL语句的效率，查看SQL语句是否覆盖索引。

二、从避免索引失效的角度。

关于如何避免索引失效，大家可以阅读我出版的《JavaGetOffer》专栏关于【MySQL索引】的文章。

三、从减少不必要的查询的角度。

如果只需要查询部分列，尽量不要使用`select *`查询，防止造成不必要的资源消耗、占用过多的网络带宽。

### 2.3 索引如何设计

> ***面试官：在工作中，表索引你怎么设计的？***

索引的设计有以下设计原则，大家在实际业务开发中应该尽量遵循这些原则，可以帮你避开不少坑。

1. 经常进行order by排序、group by分组、join多表联结查询的字段应该建立索引。

2. 经常在where子句中出现的字段应该建立索引。

3. 尽量使用数据量小的字段建立索引。例如对于char(500)和char(10)两个字段类型来说，肯定是以后者进行索引匹配的速度更快。

4. 如果需要建立索引的字段值比较长，可以使用值的部分前缀来建立索引。

   例如varchar类型的name字段，我们需要根据前三个字符来建立前缀索引，可以使用以下SQL命令：`ALTER TABLE example_table ADD INDEX index_name (name(3))`。

> ***面试官：那索引建立越多，查询效率就越高吗？***

另外大家记住一点，索引不是建立越多越好。合理设计的索引确实能大大提高SQL效率，但每建立一个字段索引，MySQL就要为该索引多维护一棵`B-Tree`，越多的索引会造成**表更新**效率变得低下。
# MySQL面试必问：索引的类型、Explain分析SQL、索引失效

## 1. 索引类型

> ***面试官：索引有什么用？***

大家可以把你最近最爱的一本书类比成一个MySQL数据库，你要快速翻到你昨天看到的精彩部分，是不是要先看下书的**目录索引**，要翻到第几章、第几页。

数据库最主要的就是数据存储，其次就是提供复杂查询服务，而索引就是MySQL作为快速找到记录的一种数据结构。索引类型有多种，像常见的B树索引、哈希索引，这些都需要我们去掌握。

不要和我说你看书都用书签，或者靠手感就能翻出来昨天看到的地方。

我们对比下不采用索引和采用索引的差异。

目前我本机数据库的article表有10w条数据，表结构如下。

```sql
CREATE TABLE `article`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `author_id` int(10) NULL DEFAULT NULL,
  `category_id` int(10) NOT NULL DEFAULT 0,
  `views` int(10) NULL DEFAULT NULL,
  `comments` int(10) NULL DEFAULT NULL,
  `title` varbinary(255) NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`id`, `category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1001 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
```

没建立索引前，使用explain关键字分析查询SQL。type显示`ALL`，也就是该SQL执行时对MySQL进行的是全表扫描。

```sql
explain select id from article where category_id = 1 order by views desc;
```

```sql
+----+-------------+---------+------+---------------+------+---------+------+------+-----------------------------+
| id | select_type | table   | type | possible_keys | key  | key_len | ref  | rows | Extra                       |
+----+-------------+---------+------+---------------+------+---------+------+------+-----------------------------+
|  1 | SIMPLE      | article | ALL  | NULL          | NULL | NULL    | NULL | 102279 | Using where; Using filesort |
+----+-------------+---------+------+---------------+------+---------+------+------+-----------------------------+
```

建立索引后。

```sql
create index idx_ca_vi on article(category_id,views);
```

type显示为`ref`，同时Extra列显示`Using where; Using index`，`Using index`代表该SQL执行时使用了索引，而`Using where`代表了在MySQL服务端再进行了一次`views`字段的排序。

```sql
+----+-------------+---------+------+---------------+-----------+---------+-------+------+-------------+
| id | select_type | table   | type | possible_keys | key       | key_len | ref   | rows | Extra       |
+----+-------------+---------+------+---------------+-----------+---------+-------+------+-------------+
|  1 | SIMPLE      | article | ref  | idx_ca_vi     | idx_ca_vi | 4       | const |    51139 | Using where; Using index |
+----+-------------+---------+------+---------------+-----------+---------+-------+------+-------------+
```

### 1.1 B-Tree索引

> ***面试官：B树索引说一下？***

在杂乱无章的一堆数字里，我要你快速找到唯一的一个数字66，大家要怎么做？

两种选择，你在一堆数字里一个个地找，就如MySQL**全表扫描**。或者把所有数都按大小顺序进行排列，找到第66个位置的数字。

我们假设建立的是主键索引，MySQL索引会根据主键id建立起一棵B-Tree。B-Tree类似于二叉搜索树，同样具有快速查找特定值的功能。

（1）但在**结构方面**，B-Tree又不同于二叉搜索树，它是**多子树的**。即每一个节点可以有两棵以上的子树。

（2）在**值的存储方面**，B-Tree所有的值都存储在叶子节点。并且每一个叶子节点可以存储多个元素，这一点也与二叉搜索树不同。两个人想要去湖里打水，一个人拿着手大的碗，一个人拿着一个水桶，拿水桶的不会比拿碗的装的少。每个叶子节点存储的元素多，每次磁盘访问就可以获得更多的数据，从而减少查询的I/O操作。

面试官经常会问你这个问题，叶子节点是什么数据结构？。实际上叶子节点之间用指针链接形成了一串**双向链表**。这个留到下文解释。

（3）另外大家很容易漏掉一个重要的知识点。如果是二级索引建立的B-Tree，每个叶子节点的值保存的是**对应行数据的主键**。那一级索引叶子节点保存什么呢？一级索引也就是主键索引，下文我会告诉大家。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/639f2a7ec6a043acb535119001e74a1e.png)

### 1.2 B-Tree值的存储

> ***面试官：你说值都存储在叶子节点，那有什么好处？***

数据库数据都存储在叶子节点，会使得非叶子节点**层数更少**。从外表来看，很明显整棵B-Tree的层数变少，B-Tree**高度变得矮胖**。

B-Tree变得矮胖有什么作用？举个爬楼梯的例子，B-Tee的每一层级就像一层楼。相信大家租房都不想租高楼，每次回去都要爬那么多层楼梯，膝盖怎么受得了呢。

B-Tree每一层的搜索可能就代表了一次磁盘I/O操作，B-Tree的层数变少意味着I/O读取的次数就变少，查询的效率也会因此提高。

另外企业业务在查询上更多的是**范围查询**，你对网页的每一次翻页操作都是对MySQL数据的一次范围查询。B-Tree的元素都存储叶子节点，同时形成双向链表结构，很适合范围查询这种复杂查询操作。

### 1.3 哈希索引

> ***面试官：知道为什么主流数据库引擎不采用哈希索引吗？***

企业业务上一般都是**范围查询**，而哈希索引由于其底层数据结构，不能够支持任何范围查询。这也难怪主流数据库引擎不青睐它。

但其实哈希索引也有它的闪光灯，哈希索引会为所有的索引列计算一个哈希码。同时在哈希表中保存哈希码和指向每个数据行的指针，这种结构对**精确匹配查询**的效率极高。

MEMORY数据库引擎底层采用的就是哈希索引。

### 1.4 聚簇索引

> ***面试官：聚簇索引和二级索引有什么关联？***

读到这里，我回答下上文还没回答大家的问题。

首先，聚簇索引和主键索引是等同的，也有一个一般都不提的名称：一级索引。

而B-Tree的**二级索引指的是非主键索引**，它的叶子节点保存的只是**行的主键值**，所以需要另外通过主键来找到行数据。

聚簇索引通过**主键来建树**，它的叶子节点包含了**行的全部数据**。

这就把两者相关联起来了，**通过二级索引查找行**，需要先在二级索引**建立**的B-Tree上找到主键的值，接着再从聚簇索引建立的B-Tree找到行数据。

## 2. 索引效率

### 2.1 Explain关键字

> ***面试官：那我一条SQL，我怎么知道它有没使用到索引？***

检查是否使用索引可以利用**Explain关键字**来分析，它会模拟执行sql语句，查询出sql语句**执行的相关信息**，如哪些索引可以被命中、哪些索引实际被命中。

我说下Explain查询结果的几个关键字段。

- **type**

  - cost：通过索引**一次**查询
  - ref：使用到索引
  - range: 使用到索引
  - all：**全表扫描**

- **Extra**

  - using filesort：使用外部文件排序，发生在无法使用索引的情况下

  - using index：**where查询**的列**被**索引覆盖，直接通过索引就可以查询到数据

  - using where：**where查询**的列，没有**全部被**索引覆盖

  - using join buffer：使用了连接缓存

- **possible_key**

  表示可以使用的索引

- **key**

  表示实际使用的索引

如果简历你写了`精通MySQL`，那问的可就没这么简单。我可以问你在工作中紧急处理了哪些数据库重大事故，优化了哪些业务慢SQL、是怎么优化的、为什么这么做。

### 2.2 索引失效

> ***面试官：有没索引失效的情况呢？***

索引失效一般是这个SQL查询破坏了**使用B-Tree查询**的条件。也有一种可能出现，如果表数据膨胀得太快，即使建立索引你查询起来也会有索引失效的错觉，这个问题就要另外讨论了。

1. 如果在where子句中使用not in、!=和＜＞操作，会使索引失效而导致进行全表扫描。

2. 对索引列进行**数学函数**处理的话，索引会失效。

3. 索引是字符串类型，查询值没有添加**单引号**''那索引会失效。因为值类型与索引列类型。不一致，MySQL**不会使用**索引，而是把索引列数据进行**类型转换**后进行查询。

4. 对索引列进行模糊查询，%要放在**最右侧**，否则索引会失效。`SELECT * FROM user WHERE name LIKE n%`

5. 在组合索引中，如果前一个索引使用**范围查询**，后面的索引也会失效。

大家在实际工作切忌乱加索引，此`切忌`非`切记`。每加一次索引，MySQL都要多去维护一棵新的B-Tree。增加太多索引，数据查询效率会变得低下。
# MySQL高阶知识：主从复制步骤、三种二进制日志格式等

## 1. MySQL主从复制

> ***面试官：MySQL主从复制了解吧？***

回答这个问题前，大家先思考下MySQL主从复制起到了什么作用。知道技术诞生的缘由，技术原理和步骤的整个逻辑推导就很清晰。

MySQL主从复制把数据库数据同步到多台服务器上，同理就可以把**读操作**分布到多台服务器上，这对于那些读密集型的系统性能提升是很大的。

数据有了多台服务器的备份，就不怕单点故障。我们只需要快速切换到另一台MySQL服务器即可，减少了数据库宕机的时间。

MySQL主从复制主要是利用了主库的**Binary Log**二进制文件来进行数据**复制**。

复制的步骤可以分为三步。

1. 主库根据**数据库事务提交的顺序**，把数据更改记录到二进制文件Binary Log中。
2. 备库建立TCP/IP连接后通过IO线程获取Binary Log，同时将Binary Log复制到中继日志**Relay Log。**
3. 备库再读取中继日志Realy Log中的事件，重放到备库的数据里。

如果你现在有两台MySQL，一台版本是03年的MySQL5.0，另一台是18年的MySQL8.0.11。新版本可以作为老版本的从服务器，但反过来是不可行的。MySQL的复制具有**向后兼容性**，老版本可能无法解析新版本的新特性，甚至复制的文件格式都差异太大。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/a17801d54fb9409bbfd00e7010545ebe.png#pic_center)

### 1.1 二进制文件的日志格式

> ***面试官：那Binary Log日志格式知道有哪些？***

MySQL提供了三种二进制日志格式用于主从复制。

1. **基于语句**的二进制文件，保存了在MySQL主库所有执行过的**数据变更语句**，相当于从库需要把主库执行过的SQL都执行一遍。
2. **基于行**的二进制文件，会把每条被改变的行记录都作为事件写入到二进制文件中。缺点也很明显，行记录的事件是很多的，可能会导致二进制文件大小过大。这种复制模式通常来说让**带宽压力**更大些。
3. 混合模式。MySQL能够在以上两种复制模式之间动态切换，默认会使用基于语句的复制方式，如果发现无法被正确复制，就切换成基于行的复制方式。

### 1.2 二进制文件选择

> ***面试官：知道哪种二进制格式比较好吗？***

**基于语句**的二进制文件，有可能会出现**数据不一致**的问题。例如某条删除语句SQL要删除10000条数据中的1000条，这条删除语句没有采用**order by**进行排序。如果主、从服务器存储数据的顺序不一样，就会导致每次执行删除的数据都是不同的。

```sql
# 没有排序的删除语句
delete from test where name = 'JavaGetOffer' limit 1000;
```

```sql
# 有排序的删除语句
delete from test where name = 'JavaGetOffer' order by id asc limit 1000;
```

混合模式的话不确定因素太多，两种复制模式的不断切换可能回导致二进制日志出现**不可预测**的事件。如果从服务器复制该二进制文件后的数据库状态是混乱无序的，那整个复制的过程就没有意义了。

一般来说选择**行的复制**会更加稳妥，也更加安全。虽然二进制文件过大会带来带宽压力大和复制较慢的问题，但比起数据安全性来说，显然后者更加重要。

### 1.3 主从模式的优点

> ***面试官：那MySQL主从模式有什么好处？***

大家如果有细看第一个`面试官问题`就知道上文已经有答案了，我这里再总结下。

1. 对于读密集的应用程序，可以利用MySQL主从模式将**读操作负载均衡**到多个从服务器上，提高系统的抗压能力。
2. MySQL主从还可以**避免单点问题**，有效减少数据库宕机的时间。同时多个数据源支持查询保证了数据库的**可用性**。
3. 另外如果需要对MySQL进行**版本升级**，可以先对备库进步版本升级，保证查询可用的同时，再慢慢升级其他全部MySQL实例。

## 2. 全局事务标识符

> ***面试官：如果把二进制文件丢给从库，从库是不是复制整个文件？***

能设计出MySQL的聪明人肯定不会这么傻。如果二进制文件包含了已存在的数据，就会造成数据重复了。

MySQL从库只会复制它本身缺失的最新数据，利用二进制文件里的**全局事务标识符(GTID)**就可以找到对应的二进制文件**具体位置**。

主库的每一次事务提交都会被分配一个唯一的全局事务标识符，这个标识由server_uuid和一个**递增**的事务编号组成。

MySQL从库是根据本身当前**全局事务标识符**找到二进制文件对应位置才进行复制而不是复制全部。
# 掌握MySQL高级特性：分区表、视图、全文索引

## 1. MySQL高级特性

### 1.1 分区表

分区的一个主要目的是将数据按照一个较粗的粒度**分在不同的区域**，这样的话就有很多好处。

1. 在执行查询的时候，优化器会根据分区定义过滤不需要查询的分区，这样的话就**不需要扫描所有数据**
2. 可以把数据分布在**不同的物理设备**上，高效利用多个硬件设备
3. 在表非常大且业务**热点数据**是最新表数据的情况下，根据时间进行分区可以快速过滤掉大量无关的历史数据

### 1.2 分区表的缺点

1. 分区表是根据**列进行分区**的话，查询那些和分区列无关的数据，需要扫描所有分区表
2. 分区列和SQL的**索引列不匹配**，也需要扫描所有分区表
3. 当对分区表增删改查时，MySQL需要**打开并锁住**所有的底层表，这是分区表的另一个开销

```sql
# 创建表时同时设置分区
CREATE TABLE sales (
    order_date DATETIME NOT NULL,
    -- Other columns omitted
) ENGINE=InnODB PARTITION BY RANGE(YEAR(order_date)) (
    PARTITION P2010 VALUES LESS THAN(2010),
    PARTITION P2011 VALUES LESS THAN(2011),
    PARTITION P2012 VALUES LESS THAN(2012),
    PARTITION pCatchall VALUES LESS THAN MAXVALUE);
```

## 2. 视图

MySQL视图本身是一个虚拟表，不存放任何数据，其实就相当于保存了一条Select语句，把这条Select语句封装成视图。

我举个例子吧。在业务开发中，如果不得不改变MySQL表名，而不想改动代码的表名。可以用视图查询新表名的内容，然后把视图命名为旧表名，这样查询视图也能查询出数据。

```sql
CREATE VIEW 新表名 AS
	SELECT * FROM 旧表名
```

## 3. 其他高级特性

MySQL高级特性还包括了存储过程、触发器和事件。

1. 存储过程其实就是在MySQL里写**方法函数**

   > 例如可以让MySQL执行函数来插入1万条数据

2. 触发器可以让你在SQL语句**操作表数据**的时候，在SQL语句执行前、执行后触发一些特定操作

   > 例如可以编写触发器，在插入A表数据时，给日志记录B表插入一条日志

3. 事件类似于**Linux的定时任务**，可以是在某个时候、每隔一个时间间隔执行一段SQL代码。

   > 例如可以创建一个事件每隔一段时间调用下我们定义的一个**存储过程**

### 3.1 全文索引

MySQL全文索引类似于ElasticSearch的全文索引。

主要是针对文本内容这种格式的数据，MySQL全文索引会对字段进行分词处理，返回匹配相关的文本内容。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/191cc611991a44feb0c937d751de3107.png#pic_center)
