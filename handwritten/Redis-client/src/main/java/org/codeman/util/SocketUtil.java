package org.codeman.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author hdgaadd
 * created on 2022/02/22
 *
 * 与Redis通信之工具类
 */
public class SocketUtil {

    private final Socket socket;

    private final InputStream inputStream;

    private final OutputStream outputStream;

    /**
     * 初始化
     *
     * @param host ip
     * @param port 端口
     * @throws IOException
     */
    public SocketUtil(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    /**
     * 将指令转换为字节数组，发送
     *
     * @param command
     * @throws IOException
     */
    public void send(String command) throws IOException {
        outputStream.write(command.getBytes());
    }

    /**
     * 将输入流的数据转换为字节数组，读取
     *
     * @return
     * @throws IOException
     */
    public String read() throws IOException{
        byte[] bytes = new byte[1024];
        int count = inputStream.read(bytes);
        return new String(bytes, 1, count);
    }
}
