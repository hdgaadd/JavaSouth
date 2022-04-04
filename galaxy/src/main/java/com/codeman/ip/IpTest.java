package com.codeman.ip;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author hdgaadd
 * Created on 2022/04/03
 * @description 获得本机ip地址
 */
public class IpTest {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost);
        System.out.println(localHost.getHostName());
        System.out.println(localHost.getHostAddress());
        System.out.println(Arrays.toString(localHost.getAddress()));
    }
}
