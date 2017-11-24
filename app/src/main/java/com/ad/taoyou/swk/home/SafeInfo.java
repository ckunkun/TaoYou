package com.ad.taoyou.swk.home;

/**
 * Created by sunweike on 2017/9/6.
 */

public class SafeInfo {
    private String tel;
    private String pwd;
    private String securityLevel;//安全级别 10低 20中 30高

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }



}
