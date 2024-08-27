> Nginx是什么？

一款**Web服务器**，它叫Nginx，碾压了Apache、Microsoft IIS、Tomact、Lighttpd等一众Web服务器。我们国内没有部署Nginx的科技业务公司，相信也没有多少。

为什么呢？南哥认为和Nginx的出身有关！Nginx在2002年立项开发就是为了服务俄罗斯访问量位居首位的Rambler.ru站点。另外最重要一点，免费开源！让Nginx集结了全球的智慧，帮助它升级迭代、不断攀登宝座。

在Java后端的每一个SpringBoot项目都集成了一个Tomcat服务器，那和Nginx有何区别？其实两者实际上都是提供互联网交互能力的一个节点，同样是Web服务器，不过主要的功能不同。

Tomcat服务器设计小巧轻量，没有集成处理复杂业务场景的功能，更适合作为一个API Web服务器。Nginx提供的功能就很多了，像反向代理、负载均衡、Web缓存，我们企业面向用户的第一关关卡便是Nginx，后面的链条才轮到微服务节点。下面我一一道来。

> Nginx常用功能知道吧？

（1）反向代理

认识Nginx就从它的反向代理功能开始，Nginx可以配置这样的映射关系。代表了所有包含`/server01/`的路径，实际指向的是后台端口为：`http://localhost:8001`。

```xml
server {
    listen       9001;
    server_name  localhost;

    location ~ /server01/ {
            proxy_pass   http://localhost:8001;
	}
    location ~ /server02/ {
            proxy_pass   http://localhost:8002;
	}
}
```

例如用户访问浏览器，这代表了用户肉眼可见的`url`实际映射到企业服务是哪个实际地址、哪些微服务节点处理这个url链条的请求等。

当然Nginx的反向代理功能不止上面说的基础功能，Nginx**转发策略**也是它的本事。我们可以设置代理的正则表达式，把一定规则的域名都转发到某一个端口。

```xml
server {
    listen 80;

    server_name example.com;

    location ~ ^/api/ {
        proxy_pass http://api.example.com;
    }
}
```

例如以上Nginx配置，南哥使用了正则表达式 `^/api/` ，严格匹配所有以 `/api/` **开头**的URL路径，我们把这些请求转发到 `http://api.example.com`。

（2）负载均衡

后台一众的微服务节点，前面我们又知道了Nginx负责代理转发的功能，那Nginx就少不了支持负载均衡。

例如6个微服务节点，1秒内1万个用户请求过来，Nginx这台Web服务器要把哪些请求转发到哪些个微服务节点，这便是负载均衡的简单理解。

Nginx服务器提供的负载均衡策略包含了内置策略、扩展策略两个类，这期我们先说说内置策略，而扩展策略顾名思义其实是第三方提供的，类似于插件。

内置策略包含了以下 3 种。

1、轮询策略

将每个用户请求（前端请求）按一定顺序逐个地代理转发到不同的微服务节点上。

2、加权策略

权是权重的意思，我们可以调整某些个后端节点的权重，性能足够的话权重可以加些，给其他节点兄弟分担分担压力。

3、IP Hash策略

这个策略对请求IP进行了Hash操作，也就是相同的Hash结果都会代理转发到同一个后端节点上。