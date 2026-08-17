package com.fhlbdm.eadvantage.util;

public class RequestContext {

    private static final ThreadLocal<String> REQUEST_ID_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ID_HOLDER = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        REQUEST_ID_HOLDER.set(requestId);
    }

    public static String getRequestId() {
        String requestId = REQUEST_ID_HOLDER.get();
        return requestId != null ? requestId : "unknown";
    }

    public static void setUserId(String userId) {
        USER_ID_HOLDER.set(userId);
    }

    public static String getUserId() {
        String userId = USER_ID_HOLDER.get();
        return userId != null ? userId : "anonymous";
    }

    public static void clear() {
        REQUEST_ID_HOLDER.remove();
        USER_ID_HOLDER.remove();
    }
}
