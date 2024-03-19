package org.codeman.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;


public class RpcServer {

    private ExecutorService threadPool;

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public RpcServer() { // 创建线程池
        // 线程池参数
        int corePoolSize = 10;
        int maximumPoolSizeSize = 100;
        long keepAliveTime = 1;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSizeSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    /**
     * 服务端主动注册服务
     */
    public void register(Object service, int port) {
        try (ServerSocket server = new ServerSocket(port)) { // 创建监听某端口的ServerSocket
            logger.info("server starts...");
            Socket socket;
            while ((socket = server.accept()) != null) { // 获取客户端发送的连接后，生成Socket
                logger.info("client connected");
                threadPool.execute(new WorkerThread(socket, service));
            }
        } catch (IOException e) {
            logger.error("occur IOException:", e);
        }
    }

}
