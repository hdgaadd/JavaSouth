package org.codeman.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author hdgaadd
 * created on 2022/11/26
 *
 * description: socket流实现客户端、服务端连通
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(8080);
        new Thread(() -> {
            while (true) {
                try {
                    Socket ac = socket.accept();

                    new Thread(() -> {
                        int len = 0;
                        byte[] arr = new byte[1024];

                        try {
                            InputStream inputStream = ac.getInputStream();
                            while ((len = inputStream.read(arr)) != -1) {
                                // offset: 为正时向右偏移
                                log.info(String.format("server receive message: %s", new String(arr, 1, len)));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
