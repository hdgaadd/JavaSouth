package org.codeman;

import java.util.Properties;

public class CommonParaUtil {

    public static Properties paraUtil() {
        try {
            Properties properties = new Properties();
            properties.load(CommonParaUtil.class.getClassLoader().getResourceAsStream("config.properties"));
            return properties;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
