package com.appril.helios.util;


import com.appril.helios.domain.ApiRequest;
import com.appril.helios.domain.JWTEntity;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;
import java.util.Date;

public class JWTUtils {


    /**
     * 校验JWT
     *
     * @param apiRequest
     * @return
     */
    public static boolean checkRequestToken(ApiRequest apiRequest) {


        String token = apiRequest.getToken();

        return checkStrToken(token);
    }

    public static boolean checkStrToken(final String token) {
        if (token != null && token.length() > 0) {
            JWTEntity jwtEntity = JWTUtils.validateJWT(token);
            return jwtEntity.getCode() == JWTEntity.JWT_CODE_OK;
        }
        return false;
    }

    /**
     * 签发JWT
     *
     * @param id
     * @param subject   可以是JSON数据 尽可能少
     *
     * @param ttlMillis
     * @return String
     */
    public static String createJWT(String id, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)   // 主题
                .setIssuer("user")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate); // 过期时间
        }
        return builder.compact();
    }

    /**
     * 验证JWT
     *
     * @param jwtStr
     * @return
     */
    public static JWTEntity validateJWT(String jwtStr) {
        JWTEntity checkResult = new JWTEntity();
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr);
            checkResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
            checkResult.setCode(JWTEntity.JWT_CODE_EXPIRE);
        } catch (SignatureException e) {
            checkResult.setCode(JWTEntity.JWT_CODE_FAIL);
        } catch (Exception e) {
            checkResult.setCode(JWTEntity.JWT_CODE_FAIL);
        }
        return checkResult;
    }

    public static SecretKey generalKey() {
        byte[] encodedKey = JWTEntity.JWT_SECERT.getBytes();
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析JWT字符串
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
