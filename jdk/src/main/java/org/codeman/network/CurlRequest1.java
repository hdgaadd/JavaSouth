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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class CurlRequest1{

    public static void main(String[] args) throws IOException {
        URL url = new URL("https://fanyi.baidu.com/v2transapi?from=zh&to=en");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Accept", "*/*");
        httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
        httpConn.setRequestProperty("Acs-Token", "1673078761120_1673165325754_NSrIx4l+iuh63hVoEIsT4WtBeriB9FNWNh//B3lTwYtYa4FN7Es1O3JyzSbayHo1crd4T7S9Cswpm4W6XlKE59jP8eroM48unMeInlkG5zXAv8juQeWxW7W/63Zo3C0xBy/vnOMbU1oH0KQKqlQWDwda9+lbIP3w13zPUsc1In3UT36d/bK0u+eAFhUJyuMx2Pf5lJDx1klLfHtoR6sz05S5gw3eFZuIbgGoNvPSYsTqdgKKSuIUxUG59umIvs7EXcIa/bafz644gA8w21agvOXv6Ix4MKb8MAwOs7wYOnuo9faKEOxfSK8HENwag/a0Mm8pAxGYyLctTFUICSnO9go8fpuHxlXGzRqUdFbtDB8=");
        httpConn.setRequestProperty("Connection", "keep-alive");
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpConn.setRequestProperty("Cookie", "APPGUIDE_10_0_2=1; REALTIME_TRANS_SWITCH=1; FANYI_WORD_SWITCH=1; HISTORY_SWITCH=1; SOUND_SPD_SWITCH=1; SOUND_PREFER_SWITCH=1; BIDUPSID=E69CFA6B9A348420A9101199EFA0AD20; PSTM=1669982029; BDUSS=VnQ3NIbXBvUnlTa3oweFJXSzRSZm5TT0xMbERxR0lkdE15c356OWZ3cklIOHBqSUFBQUFBJCQAAAAAAAAAAAEAAAAflhBv0qq6w9OmuMMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMiSomPIkqJjRF; BDUSS_BFESS=VnQ3NIbXBvUnlTa3oweFJXSzRSZm5TT0xMbERxR0lkdE15c356OWZ3cklIOHBqSUFBQUFBJCQAAAAAAAAAAAEAAAAflhBv0qq6w9OmuMMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMiSomPIkqJjRF; BAIDUID=E69CFA6B9A348420A9101199EFA0AD20:SL=0:NR=10:FG=1; ZFY=C3LN9sXZHsUEMNg0PLci1HjKezgGKMx3NKkYfhOVWm8:C; BAIDUID_BFESS=E69CFA6B9A348420A9101199EFA0AD20:SL=0:NR=10:FG=1; BAIDU_WISE_UID=wapp_1672485118113_170; __bid_n=1844b7cb9b77b1763e4207; FEID=v10-c82d81ad74f33d8d5143b62091c5c79e03bc8906; __xaf_thstime__=1672831456669; __xaf_fpstarttimer__=1672912757264; __xaf_fptokentimer__=1672912757555; FPTOKEN=x9jn1Vaxms6U79ax1ALYIYwzqGe+0ekL9obKslHXSqo8JkkZWdkpKD4ukbuM2nlV5KWZsqe9BwSwsUYNu8guFh9SSiFvOzwvtyfoSR3tOpbse04hmrecQDGh9nvknW9TvqdphynbNN8rSAMSNmdwLqmcdLnTSJhtfWzFs36w/j3yEXrIvN/1pa0g0RRO8O9/ug0zKv6zyCpxKnty2hObRk6n1SaOXma14XaJGFYT5xKJsckWAn1ca+hvMbUHC8gFcFPOyDM7hRVGvgaPgMpdF5D5YzJCbNXvVfDPJ1ZmaePo1ncJCY774yGrHQdkCu2g7q7c+wtbGzGcsX0QALfEUnYDQGmYFWN0gQHDPnKV8s1GGbIT+nbbr8d4Lb2oLOjdR8rh+ez99SIlI5lYgM1vkA==|D+WpjbZuT52NJflieNAJImM/19TLI3Z5KUYzG6jIJOM=|10|cc134594f1760015bed83de4742c11dd; RT=\"z=1&dm=baidu.com&si=a2f68d73-e950-42fa-badd-7da0b18d4abf&ss=lclvtrzv&sl=12&tt=cqq&bcn=https%3A%2F%2Ffclog.baidu.com%2Flog%2Fweirwood%3Ftype%3Dperf&ld=3xf1&nu=z8wgcsbw&cl=3pgh&ul=3ze8&hd=3zfi\"; Hm_lvt_64ecd82404c51e03dc91cb9e8c025574=1673155621,1673159717,1673162948,1673164578; Hm_lpvt_64ecd82404c51e03dc91cb9e8c025574=1673165325; ab_sr=1.0.1_OTgzNzRmMTIwMzI0NDFkNGYyZDk4Zjk2MTFmZTFjNTE5YjU5YjMyNjQ5MWRmOTc4NjhjZjg4YjRlYTRiMTUxYWEwMDk3MmQwNDA0YWExZmVkZTk4MTQxMDQ3YTAxNDA3MGMyMjZmZjg4OTc4ODE2N2VmYmE4Yjg0YTgzYTY1YThkODFjYjM2OWU2OWFhYzIwY2Q4NmQxMTg2MWI0MGM5YmE3YmY4MWUxMjQxNDdhYzliNmQxNWQ3MDQ5YjNkYzk2");
        httpConn.setRequestProperty("Origin", "https://fanyi.baidu.com");
        httpConn.setRequestProperty("Referer", "https://fanyi.baidu.com/");
        httpConn.setRequestProperty("Sec-Fetch-Dest", "empty");
        httpConn.setRequestProperty("Sec-Fetch-Mode", "cors");
        httpConn.setRequestProperty("Sec-Fetch-Site", "same-origin");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
        httpConn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        httpConn.setRequestProperty("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Google Chrome\";v=\"108\"");
        httpConn.setRequestProperty("sec-ch-ua-mobile", "?0");
        httpConn.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("from=zh&to=en&query=%E4%B8%AD%E5%9B%BD&transtype=realtime&simple_means_flag=3&sign=777849.998728&token=552c5d083c16bec4a2e8e7a3d987b28f&domain=common");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        System.out.println(response);
    }
}