package org.codeman;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hdgaadd
 * created on 2022/11/27
 *
 * design: 集群节点都注册在注册中心里，通过zookeeper广播，实现通知所有集群节点刷新缓存
 */
public class ServiceNode {

    private final static int SESSION_TIMEOUT =  5000;

    private final static String ZOOKEEPER_HOST = "127.0.0.1:2181";

    private final static String ZOOKEEPER_PATH = "/zkPath";

    private final static ExecutorService threadPool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper(ZOOKEEPER_HOST, SESSION_TIMEOUT, null);
        ZKPaths.mkdirs(zooKeeper, ZOOKEEPER_PATH);

        for (int i = 0; i < 5; i++) {
            final int index = i + 1;
            threadPool.execute(() -> {
                try {
                    createServerNode("thread-0" + index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // 等待通知
        Thread.sleep(60 * 60 * 1000);
        threadPool.shutdown();
    }

    /**
     * 模拟创建集群节点
     */
    private static void createServerNode(String serverNodeName) throws Exception {
        CuratorFramework serverNode = CuratorFrameworkFactory
                .builder()
                .connectString(ZOOKEEPER_HOST)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10, 5000))
                .build();
        serverNode.start();

        NodeCache nodeCache = new NodeCache(serverNode, ZOOKEEPER_PATH);
        nodeCache.getListenable().addListener(() -> {
            byte[] msgBytes = nodeCache.getCurrentData().getData();
            System.out.println(String.format("%s receive msg : %s", serverNodeName, new String(msgBytes, StandardCharsets.UTF_8)));

            // 业务逻辑：更新集群节点缓存
        });
        nodeCache.start();
    }
}
