package com.example.wwork4.utils;

import io.jsonwebtoken.Claims;

/**
 * 全局 JWT 上下文工具类（直接调用 JwtContext.getCurrentUserId() 获取用户信息）
 */
public class JwtContext {
    private static final ThreadLocal<Claims> CLAIMS = new ThreadLocal<>();

    // 存储 Claims（由拦截器调用）
    public static void setClaims(Claims claims) {
        CLAIMS.set(claims);
    }

    // 清理 ThreadLocal（由拦截器调用）
    public static void clear() {
        CLAIMS.remove();
    }

    // ---------- 对外暴露的快捷方法 ----------
    public static Integer getCurrentUserId() {
        Claims claims = CLAIMS.get();
        return claims != null ? claims.get("id", Integer.class) : null;
    }

    public static String getCurrentUsername() {
        Claims claims = CLAIMS.get();
        return claims != null ? claims.get("username", String.class) : null;
    }

    // 其他字段...
}