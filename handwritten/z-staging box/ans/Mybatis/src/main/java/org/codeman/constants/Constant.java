package org.codeman.constants;

/**
 * @author hdgaadd
 * created on 2022/02/23
 */
public interface Constant {
    /**
     * 字符集
     */
    String CHARSET = "UTF-8";

    /**
     * properties文件
     */
    String MAPPER_LOCATION = "mapper.location";

    String DB_DRIVER_CONF = "db.driver";

    String DB_URL_CONF = "db.url";

    String DB_PASSWORD = "db.password";

    /**
     * mapper文件后缀
     */
    String MAPPER_FILE_SUFFIX = ".xml"; // [ˈsʌfɪks]后缀

    String XML_ROOT_LABEL = "mapper"; // [ˈleɪb(ə)l]标签

    String XML_ELEMENT_ID = "id";

    String XML_SELECT_NAMESPACE = "namespace";

    String XML_SELECT_RESULTTYPE = "resultType";

}

