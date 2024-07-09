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
