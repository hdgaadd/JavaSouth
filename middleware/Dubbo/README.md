### Dubbo

**process**

- 被调用服务和调用者服务都注册到Nacos，调用者使用Dubbo从Nacos获取服务并调用
- 调用远程的对象方法，像调用本地方法一样的简单

**bugs**

- paskage -> package

- 远程接口类和远程实现类的所在的包名**必须相同**

  ```yaml
  scan:
    base-packages: com.codeman.dubbo
  ```
