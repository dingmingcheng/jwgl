package com.jwgl;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public class getData {
    public static String username = "201508180321";
    public static String password = "y12345600";
    public static String website="http://jwxt.zjyc.edu.cn/";
    public static void main(String[] args) throws ClientProtocolException, IOException {
        WebInfor infor = new WebInfor(website);
        Login login = new Login(password, username, infor);
        login.getSecret();
        //login.getsviewState();
        login.loginMainUrl();
    }
}
