package com.chinawiserv.deepone.manager.model.common;

import io.jsonwebtoken.Claims;

import java.io.Serializable;

/**
 * 认证对象
 * Created by zengpzh on 2016/6/27.
 */
public class Authorization implements Serializable{

    private static final long serialVersionUID = 2827466057629424795L;

    private String registerKey;

    private String authedToken;

    private Claims claims;

    private boolean state;

    private boolean needLogin;

    private String msg;

    public String getRegisterKey() {
        return registerKey;
    }

    public void setRegisterKey(String registerKey) {
        this.registerKey = registerKey;
    }

    public String getAuthedToken() {
        return authedToken;
    }

    public void setAuthedToken(String authedToken) {
        this.authedToken = authedToken;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
