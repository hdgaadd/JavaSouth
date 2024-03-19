package org.codeman.component;

import org.codeman.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {

    private String host;

    private int port;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, RpcClientProxy.this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName()) // 把Hello消息，转换为Socket消息
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient rpcClient = new RpcClient();
        return rpcClient.sendRpcRequest(rpcRequest, host, port); // 方法执行，修改为向端口发送消息
    }

}
