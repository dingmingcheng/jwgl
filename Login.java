package com.jwgl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.stream.FileImageOutputStream;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;

public class Login {
    private String username;
    private String password;
    private WebInfor webinfor;
    private String Cookie;
    private String viewState = "dDwyODE2NTM0OTg7Oz6jTAHYPdeY9U8Yurfs8bo0m3x8AQ==";
    private String secret;
    private String stuName;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public WebInfor getWebinfor() {
        return webinfor;
    }
    public void setWebinfor(WebInfor webinfor) {
        this.webinfor = webinfor;
    }
    
    public Login(String password, String username, WebInfor infor) {
        this.setPassword(password);
        this.setUsername(username);
        this.setWebinfor(infor);
    }
    /**
     * @throws IOException 
     * @throws ClientProtocolException 
     * 
     * 
     * */
    public void loginMainUrl() throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(webinfor.getMainUrl());
        CloseableHttpClient Client = HttpClients.createDefault();
        
        post.setHeader("Cookie",Cookie);
        List<NameValuePair> value = new ArrayList<NameValuePair>();
        value.add(new BasicNameValuePair("__VIEWSTATE",viewState));
        value.add(new BasicNameValuePair("txtUserName",username));
        value.add(new BasicNameValuePair("TextBox2",password));
        value.add(new BasicNameValuePair("RadioButtonList1","学生"));
        value.add(new BasicNameValuePair("txtSecretCode",secret));
        
        value.add(new BasicNameValuePair("hidPdrs", ""));
        value.add(new BasicNameValuePair("hidsc", ""));
        value.add(new BasicNameValuePair("Button1", ""));
        value.add(new BasicNameValuePair("lbLanguage", ""));
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(value, "UTF-8");
        post.setEntity(uefEntity);
        HttpResponse reponsePost = Client.execute(post);
        if(reponsePost.getStatusLine().getStatusCode() == 302) {
            HttpGet mainGet = new HttpGet(webinfor.getXsMainUrl()+username);
            mainGet.setHeader("Cookie",Cookie);
            mainGet.setHeader("Referer",webinfor.getMainUrl());
            
            HttpResponse reponseGet1 = Client.execute(mainGet);
            String html = gethtml(reponseGet1.getEntity().getContent());
            
            stuName = Jsoup.parse(html).getElementById("xhxm").text();
            System.out.println("登录成功！欢迎您" + stuName);
            stuName=new String(java.net.URLEncoder.encode(stuName,"utf-8").getBytes());            
        }
        else {
            System.out.println("登录失败");
        }
        Client.close();
    }
    /**
     * @throws IOException 
     * 
     * 
     * */
    public void getSecret() throws IOException {
        CloseableHttpClient Client = null;
        try {
            HttpGet secretCodeGet = new HttpGet(webinfor.getSecretUrl());
            //HttpGet secretCodeGet = new HttpGet("https://www.taobao.com");
            Client = HttpClients.createDefault();
            
            HttpResponse responseSec = null;
            responseSec = Client.execute(secretCodeGet);
            /*HeaderIterator it = responseSec.headerIterator();
            while(it.hasNext())
            {
                System.out.println(it.next());
            }*/
            Cookie = null;
            if(responseSec.getFirstHeader("Set-Cookie")!=null)
            {
                Cookie = responseSec.getFirstHeader("Set-Cookie").getValue();        
            }
            
            GifLocal(responseSec.getEntity().getContent());//验证码本地化
            
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入D:/rua.gif中的字符:");
            // 手动填充刚才获取的验证码
            secret = sc.next().trim();
            Client.close();
            //System.out.println(viewState);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Client.close();
        }
        
    }
    /**
     * 
     * 将输入流转化成本地图片
     * 
     * */
    public void GifLocal(InputStream inputstream) {
        File file = new File("D://rua.gif");
        FileImageOutputStream imageoutput = null;
        byte[] change = new byte[1024];
        int len;
        try {
            imageoutput = new FileImageOutputStream(file);
            while((len = inputstream.read(change)) != -1) {
                imageoutput.write(change, 0, len);
            }
            inputstream.close();
            imageoutput.close();            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 
     * 获得隐藏域的值
     * 
     * */
    public String gethtml(InputStream in) {
        StringBuffer ans = new StringBuffer();
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String str = null;
            while((str = read.readLine())!=null) {
                ans.append(str);
            }
            read.close();
            in.close();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ans.toString();
    }
    /**
     * @throws IOException 
     * @throws ClientProtocolException 
     * 
     * 
     * */
    public void getsviewState() throws ClientProtocolException, IOException {
        String html;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet viewget = new HttpGet(webinfor.getMainUrl());
        HttpResponse response = client.execute(viewget);
        InputStream in = response.getEntity().getContent();
        html = gethtml(in);
        viewState = Jsoup.parse(html).select("input[name=__VIEWSTATE]").val();
        System.out.println(viewState);
        client.close();
        in.close();
    }
}
