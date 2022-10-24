package org.codeman.download;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author hdgaadd
 * created on 2022/05/02
 *
 * @description: 根据url获取url对应的文件
 */
public class Url {
    private final static String url = "***";

    public static void main(String[] args) throws MalformedURLException {
        URL URL = new URL(Url.url);
        String file = URL.getFile();
    }
}
