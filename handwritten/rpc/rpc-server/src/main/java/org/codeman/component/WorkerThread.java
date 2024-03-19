package org.codeman.component;

import org.codeman.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;


public class WorkerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);

    private Socket socket;

    private Object service;

    public WorkerThread(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        // 获取连接的输入流
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream()); ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            // 获取输入流的消息
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();

            // 获取客户端需要执行的方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            // 回调函数，方法执行
            Object result = method.invoke(service, rpcRequest.getParameters());

            // 返回流
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("occur exception:", e);
        }
    }

}
