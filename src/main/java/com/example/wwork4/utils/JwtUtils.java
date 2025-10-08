package com.example.wwork4.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    // 密钥
    private static final String signKey = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6\"";
    // 过期时间
    private static final Long expire = 43200000L;

    // 生成 HMAC-SHA 密钥（新版 jjwt 要求密钥必须安全）
    private static final SecretKey key = Keys.hmacShaKeyFor(signKey.getBytes());

    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .signWith(key, Jwts.SIG.HS256)
                .expiration(new Date(System.currentTimeMillis() + expire))
                .compact();
    }

    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}