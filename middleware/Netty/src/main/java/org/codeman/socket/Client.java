package org.codeman.socket;

import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hdgaadd
 * created on 2022/11/26
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                while (true) {
                    Socket socket = new Socket("127.0.0.1", 8080);
                    String msg = String.format("halo, i am client!");

                    log.info(String.format("%s: client send message: %s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), msg));
                    socket.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
                    Thread.sleep(3000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
