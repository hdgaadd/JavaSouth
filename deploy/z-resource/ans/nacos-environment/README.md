# Nacos多环境配置

> deploy environment: Ubuntu  22.04 64位

## 1. Nacos1.4.3

- **解压**

  ```shell
  tar -zxvf nacos-server-1.4.3.tar.gz
  ```

- **关闭8848防火墙**

  ```shell
  ufw allow 8848
  ```

  ```shell
  ufw status
  ```

- **启动**

  ```shell
  cd到bin目录
  
  ubuntu系统单机必须使用: 
  bash startup.sh -m standalone
  
  ubuntu系统使用右边命令会报错web异常: sh startup.sh -m standalone
  org.springframework.context.ApplicationContextException: Unable to start web server;...
  ```

- **检查是否运行**

  ```shell
  curl http://127.0.0.1:8848/nacos
  ```

- **外网访问**

  ```
  http://ip地址:8848/nacos
  ```

- **problems**

  - Nacos1.4.3只支持jdk8，jdk11环境下无法运行

## 2. config

- springboot、spring-cloud-starter-alibaba-nacos-config、nacos的版本要对应得上

  [version reference](https://blog.csdn.net/m0_45406092/article/details/123411227)

- **yaml的变量类型为String，要添加""**

  ```yaml
  environment:
          content: halo, nacos-environment-dev
  ```

  ```yaml
  environment:
          content: "halo, nacos-environment-dev"
  ```

- **nacos配置的文件名的格式有要求**

  ```yaml
  项目模块名-命名空间环境名-文件名
  ${prefix}-${spring.profiles.active}.${file-extension}
  ```

  ```yaml
  example-dev-yaml
  
  应该修改为
  
  example-dev.yaml
  ```
