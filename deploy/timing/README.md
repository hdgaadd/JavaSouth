# 定时系统方案

> deploy environment: Ubuntu  22.04 64位

## 1. Redis集群搭建

> [reference](https://blog.csdn.net/qq_42815754/article/details/82832335)
>
> [reference](https://blog.csdn.net/qq_42815754/article/details/82912130)

- **download & install**

  ```shell
  wget http://download.redis.io/releases/redis-3.0.0.tar.gz
  
  // 安装c++
  sudo apt-get install gcc-c++
  
  tar -zxvf redis-3.0.0.tar.gz
  ```

  ```shell
  // 编译
  
  cd /usr/java/redis/redis-3.0.0
  make
  make install PREFIX=/usr/java/redis/redis-3.0.0-build
  ```

- **运行单机Redis**

  ```shell
  /usr/java/redis/redis-3.0.0-build/bin/redis-server
  ```

- **配置集群配置**

  ```shell
  // 复制conf到bin
  cd /usr/java/redis/redis-3.0.0
  cp -r /usr/java/redis/redis-3.0.0/redis.conf /usr/java/redis/redis-3.0.0-build/bin
  
  
  // 将bin文件移动到/usr/local下新创建的文件夹
  cd /usr/local
  
  mkdir redis-cluster
  
  cp -r /usr/java/redis/redis-3.0.0-build/bin redis-cluster/redis01
  
  cd /usr/local/redis-cluster/redis01
  rm -rf dump.rdb
  
  vim redis.conf
  ```

  修改配置如下，其中cluster-enabled yes的位置在632行

  ```shell
  daemonize yes
  
  port 7001
  
  ################################ REDIS CLUSTER  ###############################
  cluster-enabled yes
  ```

- **创建多个节点配置**

  ```shell
  cd /usr/local/redis-cluster
  
  cp -r redis01/ redis02
  cp -r redis01/ redis03
  cp -r redis01/ redis04
  cp -r redis01/ redis05
  cp -r redis01/ redis06
  
  每个redis0*的redis.conf的port修改为:port 700*，如redis03修改为port 7003
  ```

- **创建启动文件**

  ```shell
  cd /usr/local/redis-cluster
  
  touch start-all.sh
  
  vim start-all.sh
  ```

  ```shell
  cd redis01
  ./redis-server redis.conf
  cd ..
  cd redis02
  ./redis-server redis.conf
  cd ..
  cd redis03
  ./redis-server redis.conf
  cd ..
  cd redis04
  ./redis-server redis.conf
  cd ..
  cd redis05
  ./redis-server redis.conf
  cd ..
  cd redis06
  ./redis-server redis.conf
  cd ..
  ```

  ```shell
  // 赋予文件权限
  chmod +x start-all.sh
  ```

- **运行**

  ```shell
  ./start-all.sh
  ```

- **判断是否运行**

  ```shell
  ps -ef | grep redis
  
  root       37632       1  0 11:35 ?        00:00:01 ./redis-server *:7001 [cluster]
  root       37636       1  0 11:35 ?        00:00:01 ./redis-server *:7002 [cluster]
  root       37640       1  0 11:35 ?        00:00:01 ./redis-server *:7003 [cluster]
  root       37644       1  0 11:35 ?        00:00:01 ./redis-server *:7004 [cluster]
  root       37648       1  0 11:35 ?        00:00:01 ./redis-server *:7005 [cluster]
  root       37652       1  0 11:35 ?        00:00:01 ./redis-server *:7006 [cluster]
  root       38515   37599  0 12:03 pts/0    00:00:00 grep --color=auto redis
  ```

- **安装Redis脚本**

  ```shell
  // 安装脚本运行环境
  sudo apt update 
  sudo apt full-upgrade
  sudo apt install ruby
  
  cd /
  wget http://rubygems.org/downloads/redis-3.0.0.gem
  
  gem install redis-3.0.0.gem
  ```

- **关闭防火墙**

  ```shell
  // 关闭防火墙
  systemctl stop firewalld.service
  // 查看是否关闭
  systemctl status firewalld.service
  ```

- **开启阿里云出入口**

  入口配置reaston: 以**106.14.172.7**命令进行启动，故节点以**公网ip**进行相互访问，且节点之间访问还需使用到**节点端口 + 1000**端口

  ```shell
  阿里云配置出入口7001 ~ 7006、17001 ~ 17006
  ```

- **启动集群**

  > [reference](https://blog.csdn.net/huangxuanheng/article/details/123645185)

  ```shell
  cp -r /usr/java/redis/redis-3.0.0/src/redis-trib.rb /usr/local/redis-cluster
  
  cd /usr/local/redis-cluster
  
  apt install redis-tools
  
  // 只给内网访问
  redis-cli --cluster create 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 127.0.0.1:7006 --cluster-replicas 1
  // 只给内网访问
  redis-cli --cluster create 172.31.113.100:7001 172.31.113.100:7002 172.31.113.100:7003 172.31.113.100:7004 172.31.113.100:7005 172.31.113.100:7006 --cluster-replicas 1
  
  // 外网允许访问
  redis-cli --cluster create 106.14.172.7:7001 106.14.172.7:7002 106.14.172.7:7003 106.14.172.7:7004 106.14.172.7:7005 106.14.172.7:7006 --cluster-replicas 1
  ```

- **测试运行**

  ```shell
  cd /usr/local/redis-cluster
  
  redis-cli -c -p 7001
  
  set name hdgaadd
  get hdgaadd
  ```

- **查看集群节点个数**

  ```shell
  cluster nodes
  ```

- **关闭Redis节点**

  > 或者关闭Xshell

  ```shell
  cd /usr/local/redis-cluster
  
  touch shutdown.sh
  ```

  ```shell
  redis-cli -p 7001 shutdown
  redis-cli -p 7002 shutdown
  redis-cli -p 7003 shutdown
  redis-cli -p 7004 shutdown
  redis-cli -p 7005 shutdown
  redis-cli -p 7006 shutdown
  ```

  ```shell
  // 赋予文件权限
  chmod +x shutdown.sh
  ```

  ```shell
  ./shutdown.sh
  ```


- **删除集群缓存**

  ```shell
  touch rm.sh
  ```

  ```shell
  cd redis01
  rm -rf dump.rdb
  rm -rf nodes.conf
  cd ..
  cd redis02
  rm -rf dump.rdb
  rm -rf nodes.conf
  cd ..
  cd redis03
  rm -rf dump.rdb
  rm -rf nodes.conf
  cd ..
  cd redis04
  rm -rf dump.rdb
  rm -rf nodes.conf
  cd ..
  cd redis05
  rm -rf dump.rdb
  rm -rf nodes.conf
  cd ..
  cd redis06
  rm -rf dump.rdb
  rm -rf nodes.conf
  cd ..
  ```

  ```shell
  // 赋予文件权限
  chmod +x rm.sh
  ```

  

## 2. SpringBoot集成

> [official document](https://github.com/redisson/redisson/tree/master/redisson-spring-boot-starter#spring-boot-starter)
>
> [官网集群配置reference](https://github.com/redisson/redisson/wiki/2.-Configuration#24-cluster-mode)

- **Caused by: io.netty.channel.ConnectTimeoutException: connection timed out: 172.31.113.100/172.31.113.100:7001**

  > [reference](https://blog.csdn.net/weixin_44197039/article/details/109906059)

  修改redis01的**nodes.conf**，将172.31.113.100修改为公网





## 3. Kafka2.13-2.8.0

### (1)ubuntu

> [download](https://kafka.apache.org/downloads)
>
> [reference](https://blog.csdn.net/wanliti1314/article/details/116263788)
>
> [reference](https://blog.csdn.net/weixin_45492007/article/details/117193249)

- **启动**

  ```shell
  cd /usr/java/kafka/kafka_2.13-2.8.0
  
  // 生成集群id
  ./bin/kafka-storage.sh random-uuid
  
  // 把集群id填充，如下:
  ./bin/kafka-storage.sh format -t o_D3E4ZyRWSlszZe38IDgg -c ./config/kraft/server.properties
  
  // 启动
  ./bin/kafka-server-start.sh ./config/kraft/server.properties
  ```

- **关闭**

  ```
  窗口执行Ctrl + C
  ```

- **测试**

  ```shell
  cd /usr/java/kafka/kafka_2.13-2.8.0
  
  
  // 创建主题
  ./bin/kafka-topics.sh --create --topic kafka-topic-test --bootstrap-server localhost:9092
  
  // 进入生产者端
  ./bin/kafka-console-producer.sh --topic kafka-topic-test --bootstrap-server localhost:9092
  
  // 新开一个窗口，进入消费者端
  ./bin/kafka-console-consumer.sh --topic kafka-topic-test --from-beginning --bootstrap-server localhost:9092
  
  在生产者端发送消息，可在消费者端看见
  ```

- **knolwedge**

  - kafka2.80版本后，采用**Kraft**模式，脱离对zookeeper的依赖

### (2)windows

> [ubuntu测试不通，改用windows 安装kafka](https://blog.csdn.net/qq877507054/article/details/116355893)
>
> [bugs reference](https://blog.csdn.net/fct2001140269/article/details/84105731)

- **配置**

  ```shell
  # 配置server.properties
  
  log.dirs=D:\kafka_2.13-2.8.0\logs
  
  listeners=PLAINTEXT://localhost:9092
  advertised.listeners=PLAINTEXT://localhost:9092
  ```

- **启动**

  ```shell
  cd D:\kafka_2.13-2.8.0\bin\windows
  
  kafka-server-start.bat ..\..\config\server.properties
  ```

  

## 4. bugs

- **[ERR] Node 106.14.172.7:7006 is not empty. Either the node already knows other nodes (check with CLU**

  需要关闭Redis服务，删除redis01等的dump.rdb、nodes.conf

- **集群节点至少6个**

  ```shell
  bugs:
  *** ERROR: Invalid configuration for cluster creation.
  *** Redis Cluster requires at least 3 master nodes.
  *** This is not possible with 3 nodes and 1 replicas per node.
  *** At least 6 nodes are required.
  ```

- **Could not connect to Redis at 106.14.172.7:7001: Connection timed out**

  > [reference](https://blog.csdn.net/tutukl/article/details/104672081)

  ```shell
  // 关闭防火墙
  systemctl stop firewalld.service
  // 查看是否关闭
  systemctl status firewalld.service
  ```

- **Waiting for the cluster to join……**

  > [reference](https://www.cnblogs.com/bogiang/p/15016091.html)

  ```shell
  阿里云配置出入口7001 ~ 7006、17001 ~ 17006
  ```

- **Caused by: io.netty.channel.ConnectTimeoutException: connection timed out: 172.31.113.100/172.31.113.100:7001**

  > [reference](https://blog.csdn.net/weixin_44197039/article/details/109906059)

  修改redis01的**nodes.conf**，将172.31.113.100修改为公网

- **Unable to init enough connections amount! Only 2 of 3 were initialized. Re**

  > [reference](https://www.codeleading.com/article/15436185156/)

  修改redisson-spring-boot-starter的版本为3.16.8

## 5. knowledge

- redisson.yaml配置相关信息，@Configuration读取该redisson.yaml配置，相当于在application.yml配置redisson.yaml
- 访问Redis6379端口，服务器不会以6379端口为出口方向，向客户端发送消息，而是以主机与主机之间联结的某个端口进行通信
