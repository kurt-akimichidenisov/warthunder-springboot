package com.warthunder.vehicle.util;

/**
 * 当前登录用户上下文（ThreadLocal）
 */
public class UserContext {

    private static final ThreadLocal<Integer> currentUserId = new ThreadLocal<>();

    public static void setUserId(Integer userId) {
        currentUserId.set(userId);
    }

    public static Integer getUserId() {
        return currentUserId.get();
    }

    public static void clear() {
        currentUserId.remove();
    }
}