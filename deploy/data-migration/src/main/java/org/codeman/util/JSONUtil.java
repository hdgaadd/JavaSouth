package org.codeman.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author hdgaadd
 * created on 2022/09/24
 *
 * INSERT INTO `test`(`id`, `name`) VALUES (1, '666');
 */
public class JSONUtil {

    private static final String EXAMPLE_JSON = "{\"database\":\"hdgaadd\",\"table\":\"test\",\"type\":\"insert\",\"ts\":1663997472,\"xid\":32358,\"commit\":true,\"data\":{\"id\":1,\"name\":\"666\"}}";

    public static String JSONToSQL(String JSONStr) {
        JSONObject mainJSON = JSONObject.parseObject(JSONStr);
        String database = mainJSON.getString("database");
        String table = mainJSON.getString("table");
        String type = mainJSON.getString("type");

        String data = mainJSON.getString("data");
        JSONObject dataJSON = JSONObject.parseObject(data);
        String id = dataJSON.getString("id");
        String name = dataJSON.getString("name");

        String sql = null;
        if (type.equals("insert")) {
            sql = String.format("INSERT INTO `%s`.`%s`(`id`, `name`) VALUES (%s, '%s');", database, table, id, name);
        }
        return sql;
    }
}
