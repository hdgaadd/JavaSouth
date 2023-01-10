package org.codeman.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author hdgaadd
 * created on 2022/04/03
 *
 * description: 获得本机ip地址
 */
public class IpMsg {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost);
        System.out.println(localHost.getHostName());
        System.out.println(localHost.getHostAddress());
        System.out.println(Arrays.toString(localHost.getAddress()));
    }
}
