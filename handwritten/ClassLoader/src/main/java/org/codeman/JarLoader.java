package org.codeman;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author hdgaadd
 * created on 2022/10/09
 */
public class JarLoader extends ClassLoader{

    private static final String PATH = System.getProperty("user.dir") + "\\ClassLoader\\src\\main\\resources\\";

    private static final String CLASS_NAME = "Hello";

    private static final String CLASS_NAME_CLASS = "Hello.class";

    private static final String JAR_PATH = (PATH + CLASS_NAME + ".jar").replace("\\", "/");

    public JarLoader(String name) {
        super(name);
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
        JarFile jarFile = new JarFile(new File(JAR_PATH));
        JarEntry entry = jarFile.getJarEntry(CLASS_NAME_CLASS);

        InputStream inputStream = jarFile.getInputStream(entry);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int nextValue = 0;
        while ((nextValue = inputStream.read()) != -1) {
            outputStream.write(nextValue);
        }
        return outputStream.toByteArray();
    }

}
