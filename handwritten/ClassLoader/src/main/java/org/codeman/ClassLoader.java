package org.codeman;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author hdgaadd
 * created on 2022/10/09
 */
public class ClassLoader extends java.lang.ClassLoader {

    private static final String PATH = System.getProperty("user.dir") + "\\ClassLoader\\src\\main\\resources\\";

    private static final String CLASS_NAME = "Hello";

    private String CLASS_SUFFIX = "";

    private String CLASS_PATH = "";

    public ClassLoader(String name) {
        this.CLASS_SUFFIX = name;
        this.CLASS_PATH = (PATH + CLASS_NAME + CLASS_SUFFIX).replace("\\", "/");
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] byteArr = new byte[0];
        try {
            byteArr = getByte();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(CLASS_NAME, byteArr, 0, byteArr.length);
    }

    private byte[] getByte() throws IOException {
        FileInputStream inputStream = new FileInputStream(CLASS_PATH);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int nextValue = 0;
        while ((nextValue = inputStream.read()) != -1) {
            outputStream.write(CLASS_SUFFIX.equals(".xlass") ? 255 - nextValue : nextValue);
        }
        return outputStream.toByteArray();
    }

}
