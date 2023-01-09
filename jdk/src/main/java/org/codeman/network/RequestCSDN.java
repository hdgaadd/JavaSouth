package org.codeman.network;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hdgaadd
 * created on 2023/01/08
 *
 * https://tool.chinaz.com/tools/urlencode.aspx
 */
class RequestCSDN {

    private static final Integer PAGE_SIZE = 20;

    private static final Set<String> CONTAINER = new HashSet<>();

    private static final String MSG = "后端设计";

    private static final String KEYWORD = "设计";

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= PAGE_SIZE; i++) {
            String response = get(String.format("https://so.csdn.net/api/v3/search?q=%s&t=blog&p=%s&s=hot&tm=0&lv=-1&ft=0&l=&u=&ct=-1&pnt=-1&ry=-1&ss=-1&dct=-1&vco=-1&cc=-1&sc=-1&akt=-1&art=-1&ca=-1&prs=&pre=&ecc=-1&ebc=-1&ia=1&dId=&cl=-1&scl=-1&tcl=-1&platform=pc&ab_test_code_overlap=&ab_test_random_code=", MSG, i));
            JSONObject json0 = JSONObject.parseObject(response);
            String result_vos = json0.getString("result_vos");
            JSONArray jsonArray = JSONObject.parseArray(result_vos);
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject json1 = JSONObject.parseObject(jsonArray.get(j).toString());
                String url = "";
                String title = "";
                String description = "";
                String search_tag = "";
                String language = "";
                try {
                    url = json1.getString("url") != null ? json1.getString("url") : "";
                    title = json1.getString("title") != null ? json1.getString("title") : "";
                    description = json1.getString("description") != null ? json1.getString("description") : "";
                    search_tag = json1.getString("search_tag") != null ? json1.getString("search_tag") : "";
                    language = json1.getString("language") != null ? json1.getString("language") : "";
                } catch (Exception e) {
                    continue;
                }

                if (!CONTAINER.contains(title))
                    if (title.contains(KEYWORD) || description.contains(KEYWORD))
                        if (search_tag.contains(KEYWORD))
                            if (language.contains(KEYWORD))
                                System.out.println(String.format("page&index: %s-%s, title: %s, access url: [access url](%s)", i - 1, j, title.replaceAll("<em>", "").replaceAll("</em>", ""), url));
                CONTAINER.add(title);
            }

            Thread.sleep(600);
        }
    }

    private static String get(String url) throws Exception {
        String content = null;
        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                    (connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            content = builder.toString();
        }
        return content;
    }
}