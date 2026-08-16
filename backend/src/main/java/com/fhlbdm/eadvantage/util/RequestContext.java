package com.fhlbdm.eadvantage.util;

public class RequestContext {

    private static final ThreadLocal<String> requestIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        requestIdHolder.set(requestId);
    }

    public static String getRequestId() {
        String requestId = requestIdHolder.get();
        return requestId != null ? requestId : "unknown";
    }

    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    public static String getUserId() {
        String userId = userIdHolder.get();
        return userId != null ? userId : "anonymous";
    }

    public static void clear() {
        requestIdHolder.remove();
        userIdHolder.remove();
    }
}
