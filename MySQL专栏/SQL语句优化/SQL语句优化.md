

你说说MySQL的慢查询
要怎么优化SQL
要怎么优化索引
一条SQL是怎么运行的
创建表用外键吗



> 知道MySQL的慢查询吗？

MySQL的慢查询日志可以记录**执行时间超过阈值**的SQL查询语句，所以我们可以利用该日志查找出哪些SQL语句执行效率差，从而对SQL语句进行优化。

MySQL5.7以上版本可以通过SET命令来开启慢查询日志。

```mysql
     SET GLOBAL slow_query_log=ON;
     SET GLOBAL long_query_time=2;
     SET SESSION long_query_time=2;
```

开启完慢查询日志，我们可以找到该日志的位置，打开文件即可查询慢查询的SQL。

```mysql
     ＃　查询慢查询日志文件位置
     SHOW VARIABLES LIKE '%slow_query_log_file%';
```

![微信截图_20240508162942](D:\code\z-mine\JavaGetOffer\MySQL专栏\SQL语句优化\微信截图_20240508162942.png)

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

