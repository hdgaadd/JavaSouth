### Tomcat

**process**

- java.util.Property的load()**解析**xml文件，创建key为url，val为相关实例的Map
- 创建NIO服务器对象，装配**自定义**触发逻辑，**监听**用户请求
- ChannelHandlerContext作为管道实现响应消息的**写入**，响应用户请求

**knowledge**

- uri统一资源标识符、url统一资源定位符
- Sting对象的equalsIgnoreCase()，**忽略**两者的大小写，而进行字符串比较
- **java.util.Property**的load()，可以**解析xml文件**，解析格式以"="进行切割