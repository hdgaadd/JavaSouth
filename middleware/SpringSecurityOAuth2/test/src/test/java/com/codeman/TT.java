package com.codeman;

import com.gargoylesoftware.htmlunit.HttpMethod;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class TT {

    public static String authorization_server = "http://127.0.0.1:6001/";
    public static String resource_server = "http://127.0.0.1:6002/";

    // 密码模式
    @Test
    public void getTokenByPassword() throws IOException {
        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "password",
                "username", "admin",
                "password", "admin"
        };
        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/token", null, null, params);
    }

    // 验证令牌
    @Test
    public void checkToken() throws IOException {
        String[] params = new String[]{
                "token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UxIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUxIl0sImV4cCI6MTY0MTkxNzQzMSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiYWNhN2FhYzctZjMxNS00YjRkLWIzNzItYTM3YTY3M2ZhNWZiIiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.zUFV6K4JqlvVSUp-kNH4Zdf4DxnnB59MhZzTihLe4ik",
        };
        HttpUtil.send(HttpMethod.POST, authorization_server + "oauth/check_token", null, null, params);
        }

    // 拿取令牌访问资源服务器
    @Test
    public void getResourceByToken() throws IOException {
        Map<String, String> head = RootUtil.buildMap("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UxIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsic2NvcGUxIl0sImV4cCI6MTY0MTkxNzQzMSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiYWNhN2FhYzctZjMxNS00YjRkLWIzNzItYTM3YTY3M2ZhNWZiIiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.zUFV6K4JqlvVSUp-kNH4Zdf4DxnnB59MhZzTihLe4ik");
        HttpUtil.send(HttpMethod.POST,resource_server + "/admin",head,null);
    }
}