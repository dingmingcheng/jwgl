package com.jwgl;

public class WebInfor {
    private String mainUrl;
    private String loginUrl;
    private String secretUrl;
    private String xsMainUrl;
    public String getXsMainUrl() {
        return xsMainUrl;
    }

    public void setXsMainUrl(String xsMainUrl) {
        this.xsMainUrl = xsMainUrl;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSecretUrl() {
        return secretUrl;
    }

    public void setSecretUrl(String secretUrl) {
        this.secretUrl = secretUrl;
    }

    public WebInfor(String website) {
        mainUrl = website;
        init();
    }
    
    public void init() {
        this.secretUrl = mainUrl + "CheckCode.aspx" ;
        this.xsMainUrl = mainUrl + "xs_main.aspx?xh=";
    }
}
