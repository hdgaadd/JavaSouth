package com.codeman;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hdgaadd
 * Created on 2022/05/02
 */
public class ReadDirectory {
    /**
     * 忽略的文件夹
     */
    private static List<String> ignoreDirectory = new ArrayList<String>(){{
        add(".git");
        add(".idea");
    }};

    public static void main(String[] args) {
        // 项目文件对象
        File parentFile = new File(System.getProperty("user.dir"));

        for (File childFile : parentFile.listFiles()) {
            if (!ignoreDirectory.contains(getPostfix(childFile)) && childFile.isDirectory()) {
                System.out.println(getPostfix(childFile));

                for (File grandsonFile : childFile.listFiles()) {
                    if (grandsonFile.isDirectory()) {
                        System.out.print("----");
                        System.out.println(getPostfix(grandsonFile));
                    }
                }
            }
        }
    }

    /**
     * 从文件物理地址中，获取文件夹名
     *
     * @param file
     * @return
     */
    private static String getPostfix(File file) {
        String[] split = file.toString().split("\\\\");
        return split[split.length - 1];
    }
}
