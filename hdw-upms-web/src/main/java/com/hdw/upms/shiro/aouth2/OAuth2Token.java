package com.hdw.upms.shiro.aouth2;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author TuMinglong
 * @version 1.0.0
 * @Description OAuth2Token
 * @date 2018年6月11日下午4:55:44
 */
public class OAuth2Token implements AuthenticationToken {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
