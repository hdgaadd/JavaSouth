## Gateway

**process**

- 客户端服务在Nacos进行注册，网关从Nacos获取客户端服务

  用户访问网关，即可访问客户端服务

**knowledge**

- bootstrap.yml使用于Spring Cloud的应用上下文，application.yml使用于Spring Boot的应用上下文

  bootstrap.yml先于application.yml加载
