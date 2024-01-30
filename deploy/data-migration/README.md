# 数据迁移方案

> deploy environment: Ubuntu 20.04 64位

## 1. JRE11.0.16

- **创建文件夹**

  ```shell
  cd /usr、mkdir java、cd java、mkdir jdk
  ```

- **上传jdk**

  ```shell
  cd /usr/java/jdk/jdk-11.0.16
  ```

- **解压**

  ```shell
  tar -zxvf jdk-11.0.16_linux-x64_bin.tar.gz
  ```

- **环境变量**

  - 创建脚本

    ```shell
    touch /etc/profile.d/java.sh
    ```

  - 按i编辑

    ```shell
    vim /etc/profile.d/java.sh
    
    JAVA_HOME=/usr/java/jdk/jdk-11.0.16
    CLASSPATH=$JAVA_HOME/lib
    PATH=$JAVA_HOME/bin:$PATH
    export PATH JAVA_HOME CLASSPATH
    
    :wq
    ```

  - 使配置环境生效

    ```shell
    source /etc/profile.d/java.sh
    ```

  - java -version

- **删除jdk11**

  删除安装文件夹、删除java.sh



## 2. MySQL8.0.30(Debian)

> [reference](https://blog.csdn.net/wavehaha/article/details/114730222)

- **安装**

  ```shell
  sudo apt-get update
  
  sudo apt-get install mysql-server
  ```

- **检查是否运行**

  ```shell
  systemctl status mysql.service
  ```

- **设置密码**

  ```shell
  sudo mysql_secure_installation
  ```

- **登录**

  ```shell
  在任何地方执行以下
  
  mysql -uroot -proot
  ```

- **knowledge**

  - 以下打印，是询问**是否**使用**密码强度判断组件**，若选择是，则root等简单密码无法设置

    ```
    Securing the MySQL server deployment.
    
    Connecting to MySQL using a blank password.
    
    VALIDATE PASSWORD COMPONENT can be used to test passwords
    and improve security. It checks the strength of password
    and allows the users to set only those passwords which are
    secure enough. Would you like to setup VALIDATE PASSWORD component?
    
    Press y|Y for Yes, any other key for No: 
    ```

    

- **bugs**

  E: Unable to locate package MySQL-server

  ```shell
  再执行以下命令
  
  sudo apt-get update
  
  sudo apt-get install mysql-server
  ```

- **卸载**

  > [reference](https://blog.csdn.net/w3045872817/article/details/77334886)

  ```shell
  查看安装依赖项
  dpkg --list|grep mysql
  
  sudo apt-get remove mysql-common
  
  sudo apt-get autoremove --purge mysql-server-8.0
  
  清除残留数据
  dpkg -l |grep ^rc|awk '{print $2}' |sudo xargs dpkg -P
  ```





## 3. Maxwell1.38.0

> [official website](https://maxwells-daemon.io/quickstart/)
>
> [reference](https://blog.csdn.net/Allenzyg/article/details/105810760)

- **配置binlog**

  ```shell
  vim /etc/my.cnf
  ```

  ```shell
  [mysql_d]
  server_id=1
  log-bin=master
  binlog_format=row
  ```

  ```shell
  service mysql restart
  ```

- **MySQL创建maxwell用户**

  ```shell
  mysql -uroot -proot
  ```

  以下直接沾沾即可全部执行

  ```shell
  CREATE database maxwell;
  CREATE USER 'maxwell'@'%' IDENTIFIED BY 'maxwell';
  GRANT ALL ON maxwell.* TO 'maxwell'@'%';
  GRANT SELECT, REPLICATION CLIENT, REPLICATION SLAVE ON *.* TO 'maxwell'@'%';
  flush privileges;
  ```

- **编辑maxwell配置文件**

  ```shell
  cd /usr/java/maxwell/maxwell-1.38.0
  
  mv config.properties.example config.properties
  
  vim config.properties
  
  任性以下两种配置，推荐two
  ```

- **两种配置，两种运行方式**

  > 建议two

  - **one**

    ```shell
    log_level=info
    
    # mysql login info
    host=localhost
    ```

    ```shell
    bin/maxwell --user='maxwell' --password='maxwell' --host='127.0.0.1' --producer=stdout
    ```

  - **two**

    ```shell
    log_level=info
    
    # mysql login info
    host=localhost
    user=maxwell
    password=maxwell
    ```

    ```shell
    bin/maxwell --config=./config.properties
    ```

- **创建非maxwell数据库，编写SQL测试，注意是非maxwell数据库**

  ```shell
  CREATE DATABASE hdgaadd;
  
  use hdgaadd;
  
  DROP TABLE IF EXISTS `test`;
  CREATE TABLE `test`  (
    `id` int(11) NULL DEFAULT NULL,
    `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL
  ) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;
  
  INSERT INTO `test`(`id`, `name`) VALUES (1, '1');
  UPDATE `test` SET `id` = 1, `name` = '1333333333' WHERE `id` = 1 LIMIT 1;
  DELETE FROM `test`;
  ```

- **bugs**

  - **测试数据库必须使用非maxwell**

    若**使用maxwell数据库**，会出现插入、更新表，maxwell**没有监听**到

    ```mysql
    CREATE DATABASE hdgaadd
    
    user hdgaadd;
    ```

  - **maxwell-1.38.0要求的jdk版本必须 >= 11**

    ```shell
    Error: A JNI error has occurred, please check your installation and try again
    Exception in thread "main" java.lang.UnsupportedClassVersionError: com/zendesk/maxwell/Maxwell has been compiled by a more recent version of the Java Runtime (class file version 55.0), this version of the Java Runtime only recognizes class file versions up to 52.0
    ```

  - **ERROR: Operation CREATE USER failed for 'maxwell'@'%**

    ```shell
    删除该用户后创建
    drop user maxwell
    
    若是以下异常，则执行以下命令
    ERROR: Operation CREATE USER failed for 'maxwell'@'%localhos'
    drop user 'maxwell'@'localhost';
    ```

- **knowledge**

  - **binlog是ubuntu的日志文件，mysql-bin是win的日志文件**

    - win

      ```shell
      show master logs;
      +------------------+-----------+
      | Log_name         | File_size |
      +------------------+-----------+
      | mysql-bin.000001 |       393 |
      +------------------+-----------+
      ```

    - ubuntu

      ```shell
      show master logs;
      +---------------+-----------+-----------+
      | Log_name      | File_size | Encrypted |
      +---------------+-----------+-----------+
      | binlog.000001 |       180 | No        |
      | binlog.000002 |       404 | No        |
      | binlog.000003 |       180 | No        |
      | binlog.000004 |       180 | No        |
      | binlog.000005 |       404 | No        |
      | binlog.000006 |      1522 | No        |
      +---------------+-----------+-----------+
      ```

  - **bin设置成什么，日志文件就是什么名称**

    ```shell
    binlog_format=row
    server_id=1 
    log-bin=master
    ```

    ```shell
    mysql> show master logs;
    +---------------+-----------+-----------+
    | Log_name      | File_size | Encrypted |
    +---------------+-----------+-----------+
    | master.000001 |       498 | No        |
    +---------------+-----------+-----------+
    1 row in set (0.00 sec)
    
    ```

  - **查看MySQL日志文件**

    > [reference](https://www.jianshu.com/p/3eb4c44307c1)

    ```shell
    // 查看是否设置日志
    show variables like 'log_%';
    
    // 展示所有biglog，最底下的最新
    show master logs;
    +---------------+-----------+-----------+
    | Log_name      | File_size | Encrypted |
    +---------------+-----------+-----------+
    | binlog.000001 |       180 | No        |
    | binlog.000002 |       404 | No        |
    | binlog.000003 |       180 | No        |
    | binlog.000004 |    111875 | No        |
    +---------------+-----------+-----------+
    
    // 删除所有biglog
    reset master; 
    
    // 打印某个biglog，ubuntu默认是在/var/lib/mysql/binlog
    /usr/bin/mysqlbinlog /var/lib/mysql/binlog.000005
    
    // 由于Base64编码，导致SQL打印看不清楚，可使用以下命令（打印最后一个binlog可查看最新记录）
    /usr/bin/mysqlbinlog --base64-output=decode-rows -v /var/lib/mysql/binlog.000001
    ```

  - **Linux中binlog的文件地址**

    ```shell
    cd /var/lib/mysql/
    ```




## 4. RabbitMQ(Debian)

> [reference address](https://www.jianshu.com/p/5c8c4495827f)

- **安装erlong语言环境**

  ```shell
  sudo apt-get install erlang-nox
  
  检查是否成功安装
  erl
  ```

- **添加公钥**

  ```shell
  wget -O- https://www.rabbitmq.com/rabbitmq-release-signing-key.asc | sudo apt-key add -
  ```

- **安装**

  ```shell
  sudo apt-get update
  
  sudo apt-get install rabbitmq-server
  ```

- **检查是否运行**

  ```shell
  systemctl status rabbitmq-server
  ```

- **启动、停止**

  ```shell
  sudo service rabbitmq-server start    # 启动
  sudo service rabbitmq-server stop     # 停止
  sudo service rabbitmq-server restart  # 重启 
  ```

- **安装管理界面**

  > [reference](https://blog.csdn.net/fanbaodan/article/details/103335793)
  > [reference](https://blog.csdn.net/z446981439/article/details/103634524)

  ```shell
  rabbitmq-plugins enable rabbitmq_management
  
  创建用户
  rabbitmqctl add_user hdgaadd root
  设置管理员角色
  rabbitmqctl set_user_tags hdgaadd administrator
  ```

  ```shell
  测试访问
  curl http://IP地址:15672
  ```

- **基于以上对MySQL的配置，再进行配置RabbitMQ**

  > [reference](https://blog.csdn.net/xiehd313/article/details/81289150)

  ```shell
  cd /usr/java/maxwell/maxwell-1.38.0
  
  vim config.properties
  ```

  ```shell
  producer=rabbitmq
  
  rabbitmq_host=localhost
  rabbitmq_port=5672
  rabbitmq_user=hdgaadd
  rabbitmq_pass=root
  rabbitmq_virtual_host=/
  rabbitmq_exchange=maxwell
  rabbitmq_exchange_type=topic
  rabbitmq_exchange_durable=false
  rabbitmq_exchange_autodelete=false
  rabbitmq_routing_key_template=%db%.%table%
  rabbitmq_message_persistent=false
  ```

  以下为**备份**的完整文件

  ```shell
  # tl;dr config
  log_level=info
  
  #producer=rabbitmq
  #kafka.bootstrap.servers=localhost:9092
  
  # mysql login info
  host=localhost
  user=maxwell
  password=maxwell
  
  
  producer=rabbitmq
  
  rabbitmq_host=localhost
  rabbitmq_port=5672
  rabbitmq_user=hdgaadd
  rabbitmq_pass=root
  rabbitmq_virtual_host=/
  rabbitmq_exchange=maxwell
  rabbitmq_exchange_type=topic
  rabbitmq_exchange_durable=false
  rabbitmq_exchange_autodelete=false
  rabbitmq_routing_key_template=%db%.%table%
  rabbitmq_message_persistent=false
  ```

- **运行maxwell**

  ```shell
  cd /usr/java/maxwell/maxwell-1.38.0
  
  bin/maxwell --config=./config.properties
  ```

- **登录RabbitMQ，查看消息**

  > [reference](https://blog.csdn.net/xiehd313/article/details/81289150)

  - Queues模块下: 创建队列，队列名以以上的**rabbitmq_routing_key_template=%db%.%table%**配置为标准，故队列名为

    ```shell
    hdgaadd.test
    ```

  - Exchanges模块下: 点击maxwell，绑定以上队列，点击GetMessage发送消息
