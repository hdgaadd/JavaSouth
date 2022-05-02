package com.codeman.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author hdgaadd
 * Created on 2022/04/26
 */
public class Url {
    public static void main(String[] args) throws IOException {
        String url = "http://10.44.20.232/rainwater/水情日报2022年04月22日.pdf";
        URL URL = new URL(url);
        String file = URL.getFile();
        File file1 = new File(String.valueOf(URL));

        System.out.println(file1);
    }
}
