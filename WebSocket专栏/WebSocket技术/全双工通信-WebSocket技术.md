## 1. WebSocket概念

### 1.1 为什么会出现WebSocket

一般的Http请求我们只有主动去请求接口，才能获取到服务器的数据。例如前后端分离的开发场景，自嘲为切图仔的前端大佬找你要一个`配置信息`的接口，我们后端开发三下两下开发出一个`RESTful`架构风格的API接口，只有当前端主动请求，后端接口才会响应。

但上文这种基于HTTP的**请求-响应**模式并不能满足**实时数据通信**的场景，例如游戏、聊天室等实时业务场景。现在救世主来了，WebSocket作为一款主动推送技术，可以实现服务端主动推送数据给客户端。大家有没听说过全双工、半双工的概念。

> 全双工通信允许数据同时双向流动，而半双工通信则是数据交替在两个方向上传输，但在任一时刻只能一个方向上有数据流动

HTTP通信协议就是半双工，而数据实时传输需要的是全双工通信机制，WebSocket采用的便是全双工通信。举个微信聊天的例子，企业微信炸锅了，有**成百条消息轰炸**你手机，要实现这个场景，大家要怎么设计？用iframe、Ajax异步交互技术配合以客户端**长轮询**不断请求服务器数据也可以实现，但造成的问题是服务器资源的**无端消耗**，运维大佬直接找到你工位来。显然服务端主动推送数据的WebSocket技术更适合聊天业务场景。

### 1.2 WebSocket优点

大家先看看传统的Ajax长轮询和WebSocket性能上掰手腕谁厉害。在websocket.org网站提供的`Use Case C`的测试里，客户端轮询频率为10w/s，使用Poling长轮询每秒需要消耗高达665Mbps，而我们的新宠儿WebSocet仅仅只需要花费1.526Mbps，435倍的差距！！

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/872b8bcd6ed04404bfa2f3343e1e6054.png#pic_center)


为什么差距会这么大？南哥告诉你，WebSocket技术设计的目的就是要取代轮询技术和Comet技术。Http消息十分冗长和繁琐，一个Http消息就要包含了起始行、消息头、消息体、空行、换行符，其中**请求头Header**非常冗长，在大量Http请求的场景会占用过多的带宽和服务器资源。

大家看下百度翻译接口的Http请求，拷贝成curl命令是非常冗长的，可用的消息肉眼看过去没多少。

```sh
curl ^"https://fanyi.baidu.com/mtpe-individual/multimodal?query=^%^E6^%^B5^%^8B^%^E8^%^AF^%^95&lang=zh2en^" ^
  -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7" ^
  -H "Accept-Language: zh-CN,zh;q=0.9" ^
  -H "Cache-Control: max-age=0" ^
  -H "Connection: keep-alive" ^
  -H ^"Cookie: BAIDUID=C8FA8569F446CB3F684CCD2C2B32721E:FG=1; BAIDUID_BFESS=C8FA8569F446CB3F684CCD2C2B32721E:FG=1; ab_sr=1.0.1_NDhjYWQyZmRjOWIwYjI3NTNjMGFiODExZWFiMWU4NTY4MjA2Y2UzNGQwZjJjZjI1OTdlY2JmOThlNzk1ZDAxMDljMTA2NTMxYmNlM1OTQ1MTE0ZTI3Y2M0NTIzMzdkMmU2MGMzMjc1OTRiM2EwNTJQ==; RT=^\^"z=1&dm=baidu.com&si=b9941642-0feb-4402-ac2b-a913a3eef1&ss=ly866fx&sl=4&tt=38d&bcn=https^%^3A^%^2F^%^2Ffclog.baidu.com^%^2Flog^%^2Fweirwood^%^3Ftype^%^3Dp&ld=ccy&ul=jes^\^"^" ^
  -H "Sec-Fetch-Dest: document" ^
  -H "Sec-Fetch-Mode: navigate" ^
  -H "Sec-Fetch-Site: same-origin" ^
  -H "Sec-Fetch-User: ?1" ^
  -H "Upgrade-Insecure-Requests: 1" ^
  -H "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36" ^
  -H ^"sec-ch-ua: ^\^"Not/A)Brand^\^";v=^\^"8^\^", ^\^"Chromium^\^";v=^\^"126^\^", ^\^"Google Chrome^\^";v=^\^"126^\^"^" ^
  -H "sec-ch-ua-mobile: ?0" ^
  -H ^"sec-ch-ua-platform: ^\^"Windows^\^"^" &
```

而WebSocket是基于帧传输的，只需要做一次握手动作就可以让客户端和服务端形成一条通信通道，这仅仅只需要2个字节。我搭建了一个SpringBoot集成的WebSocket项目，浏览器拷贝WebSocket的Curl命令十分简洁明了，大家对比下。

```sh
curl "ws://localhost:8080/channel/echo" ^
  -H "Pragma: no-cache" ^
  -H "Origin: http://localhost:8080" ^
  -H "Accept-Language: zh-CN,zh;q=0.9" ^
  -H "Sec-WebSocket-Key: VoUk/1sA1lGGgMElV/5RPQ==" ^
  -H "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36" ^
  -H "Upgrade: websocket" ^
  -H "Cache-Control: no-cache" ^
  -H "Connection: Upgrade" ^
  -H "Sec-WebSocket-Version: 13" ^
  -H "Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits"
```

如果你要区分Http请求或是WebSocket请求很简单，WebSocket请求的请求行前缀都是固定是`ws://`。

## 2. WebSocket实践

### 2.1 集成WebSocket服务器

大家要在SpringBoot使用WebSocket的话，可以集成`spring-boot-starter-websocket`，引入南哥下面给的pom依赖。

```xml
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-websocket</artifactId>
		</dependency>
	</dependencies>
```

感兴趣点开`spring-boot-starter-websocket`依赖的话，你会发现依赖所引用包名为`package jakarta.websocket`。这代表SpringBoot其实是集成了Java EE开源的websocket项目。这里有个小故事，Oracle当年决定将Java EE移交给Eclipse基金会后，Java EE就进行了改名，现在Java EE更名为Jakarta EE。Jakarta是雅加达的意思，有谁知道有什么寓意吗，评论区告诉我下？

我们的程序导入websocket依赖后，应用程序就可以看成是一台小型的WebSocket服务器。我们通过@ServerEndpoint可以定义WebSocket服务器对客户端暴露的接口。

```java
@ServerEndpoint(value = "/channel/echo")
```

而WebSocket服务器要推送消息给到客户端，则使用`package jakarta.websocket`下的Session对象，调用`sendText`发送服务端消息。

```java
    private Session session;
    
    @OnMessage
    public void onMessage(String message) throws IOException{
        LOGGER.info("[websocket] 服务端收到客户端{}消息：message={}", this.session.getId(), message);
        this.session.getAsyncRemote().sendText("halo, 客户端" + this.session.getId());
    }
```

看下`getAsyncRemote`方法返回的对象，里面是一个远程端点实例。

```java
    RemoteEndpoint.Async getAsyncRemote();
```

### 2.2 客户端发送消息

客户端发送消息要怎么操作？这点还和Http请求很不一样。后端开发出接口后，我们在Swagger填充参数，点击`Try it out`，Http请求就发过去了。

但WebSocket需要我们在浏览器的控制台上操作，例如现在南哥要给我们的WebSocket服务器发送`Halo，JavaGetOffer`，可以在浏览器的控制台手动执行以下命令。

```sh
websocket.send("Halo，JavaGetOffer");
```

实践的操作界面如下。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/1ff67a75514f4f87ac0ffc01a05b0017.png#pic_center)
