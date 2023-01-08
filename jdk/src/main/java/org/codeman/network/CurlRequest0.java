package org.codeman.network;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @author hdgaadd
 * created on 2023/01/08
 *
 * https://curlconverter.com/java/
 * copy as bash
 */
class CurlRequest0 {

    private static final String MSG = "设计";

    private static final String KEYWORD = "设计";

    private static final int PAGE_INDEX = 60;

    private static final Set<String> CONTAINER = new HashSet<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < PAGE_INDEX; i++) {
            URL url = new URL("https://api.juejin.cn/search_api/v1/search?aid=2608&uuid=7162897933325780510&spider=0");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");

            httpConn.setRequestProperty("authority", "api.juejin.cn");
            httpConn.setRequestProperty("accept", "*/*");
            httpConn.setRequestProperty("accept-language", "zh-CN,zh;q=0.9");
            httpConn.setRequestProperty("content-type", "application/json");
            httpConn.setRequestProperty("cookie", "");
            httpConn.setRequestProperty("origin", "https://juejin.cn");
            httpConn.setRequestProperty("referer", "https://juejin.cn/");
            httpConn.setRequestProperty("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Google Chrome\";v=\"108\"");
            httpConn.setRequestProperty("sec-ch-ua-mobile", "?0");
            httpConn.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
            httpConn.setRequestProperty("sec-fetch-dest", "empty");
            httpConn.setRequestProperty("sec-fetch-mode", "cors");
            httpConn.setRequestProperty("sec-fetch-site", "same-site");
            httpConn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");

            httpConn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write("{\"key_word\":\"" + MSG + "\",\"id_type\":0,\"cursor\":\"" + (i * 20) + "_20230108151709CF7AE10D3FD977F4A734\",\"limit\":20,\"search_type\":0,\"sort_type\":0,\"version\":1,\"uuid\":\"\",\"ab_info\":\"}\"}");
            writer.flush();
            writer.close();
            httpConn.getOutputStream().close();

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            JSONObject json0 = JSONObject.parseObject(response);
            JSONArray data = json0.getJSONArray("data");

            for (int j = 0; j < 20; j++) {
                JSONObject json1 = JSONObject.parseObject(data.get(j).toString());
                JSONObject json2 = json1.getJSONObject("result_model");
                JSONObject json3 = JSONObject.parseObject(json2.getString("article_info"));
                JSONObject json4 = JSONObject.parseObject(json2.getString("category"));

                String article_id = "";
                String title = "";
                String brief_content = "";
                String category_name = "";
                try {
                    article_id = json3.getString("article_id");
                    title = json3.getString("title");
                    brief_content = json3.getString("brief_content");
                    category_name = json4.getString("category_name");
                } catch (Exception e) {
                    continue;
                }

                if (!CONTAINER.contains(title))
                    if (title.contains(KEYWORD) || brief_content.contains(KEYWORD))
                        if (category_name.equals("后端"))
                            System.out.println(String.format("page&index: %s-%s, title: %s , access url: %s", i, j, title, "https://juejin.cn/post/" + article_id));
                CONTAINER.add(title);
            }

            // limit
            Thread.sleep(700);
        }
    }
}
