package com.chinawiserv.deepone.manager.model.common;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zengpzh on 2016/6/20.
 */
public class JwtInfo implements Serializable{

    private static final long serialVersionUID = -8920657136187754391L;

    public JwtInfo(String token, String loginName, String issuer, HttpServletRequest request) throws Exception{
        if(StringUtils.isBlank(token) || StringUtils.isBlank(loginName) || StringUtils.isBlank(issuer) || request == null) throw new Exception("不完整的Jwt信息！");
        this.token = token;
        this.loginName = loginName;
        this.issuer = issuer;
        this.request = request;
        this.latestRequest = request.getMethod() + " " + request.getRequestURI();
    }

    private String token;

    private String loginName;

    private String issuer;

    private Date expDate = new Date();

    private HttpServletRequest request;

    private String latestRequest;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getLatestRequest() {
        return latestRequest;
    }

    public void setLatestRequest(String latestRequest) {
        this.latestRequest = latestRequest;
    }

    public void setLatestRequest(HttpServletRequest request) {
        if(request != null) this.latestRequest = request.getMethod() + " " + request.getRequestURI();
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        else if(!(obj instanceof JwtInfo)) return false;
        else this.toString().equals(obj.toString());
        return false;
    }

    @Override
    public String toString(){
        return this.loginName + " - " + issuer;
    }
}