package com.appril.helios.domain;

import io.jsonwebtoken.Claims;

public class JWTEntity {

    public static final String JWT_SECERT = "JWTSECERT";
    public static final int JWT_CODE_OK = 1;
    public static final int JWT_CODE_EXPIRE = -1001;
    public static final int JWT_CODE_FAIL = -1000;
    public static final long JWT_TTL = 86400000L;


    private Claims claims;
    private int code = JWTEntity.JWT_CODE_OK;


    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
