package com.jimu.study.common;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author hxt
 * @date 2020/1/20 8:44
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
