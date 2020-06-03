# ***Spring集成WebSocket即时推送***

## 一、后端

### 1.代码部分

(1) 新建`WebSocketHandler`，实现`WebSocketHandler接口`或继承`TextWebSocketHandler类`

​     若实现`WebSocketHandler`则需实现其中方法

```java
// 建立连接
@Override
public void afterConnectionEstablished(WebSocketSession session) throws Exception
{}

// 从客户端获取消息
@Override
public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
    throws Exception
    {}

// 连接异常
@Override
public void handleTransportError(WebSocketSession session, Throwable exception)
    throws Exception
    {}

// 连接关闭
@Override
public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
    throws Exception
    {}

// 消息是否分片
@Override
public boolean supportsPartialMessages()
{
    return false;
}
```

​     若继承`TextWebSocketHandler`则重写其中方法

```java
// 从客户端接收到文本消息
@Override
protected void handleTextMessage(WebSocketSession session, TextMessage message)
    throws Exception
{}
```

(2) 新建`WebSocketInterceptor,`实现`HandshakeInterceptor接口`或继承`HttpSessionHandshakeInterceptor`

```java
// 客户端发起握手连接前
@Override
public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception
{
    /** 入参attributes对应WebSocketHandler中的WebSocketSession对象中的attributes属性
     *  此处设置的attributes将覆盖原本的attributes
     */
}

// 
@Override
public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception)
{}
```

### 2.配置部分

(1) Spring配置头加上如下配置:

```xml
xmlns:websocket="http://www.springframework.org/schema/websocket"
xsi:schemaLocation="
http://www.springframework.org/schema/websocket http://www.springframework.org/schema/websocket/spring-websocket.xsd"
```

(2) 将`WebSocketHandler`和`WebSocketInterceptor`注册进Spring配置文件

```xml
<!-- 注册处理websocket请求的handler -->
<bean id="messageWebsocketHandler" class="com.woke.websocket.WebsocketHandler"/>

<!-- 配置websocket拦截器，所有被识别为websocket的请求都会进入此拦截器 -->
<!-- allowd-origins="*"为拦截所有请求，否则会因为跨域问题不进入对应handler -->
<websocket:handlers allowed-origins="*">
    <!-- 配置handler对应的url -->
    <websocket:mapping path="/message" handler="messageWebsocketHandler"/>
    <!-- 配置对应拦截器 -->
    <websocket:handshake-interceptors>
        <bean id="websocketInterceptor" class="com.woke.websocket.WebsocketInterceptor" />
    </websocket:handshake-interceptors>
</websocket:handlers>
```



## 二、前端