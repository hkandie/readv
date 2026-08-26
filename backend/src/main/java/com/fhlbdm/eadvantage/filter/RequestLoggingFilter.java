package com.fhlbdm.eadvantage.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fhlbdm.eadvantage.util.RequestContext;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String USER_ID_KEY = "userId";

    // Per OWASP ESAPI logging guidance: strip CR/LF and other control characters
    // from externally-influenced values before they enter MDC, to prevent log
    // forging/injection (CWE-117) via headers like X-Request-ID or auth principal names.
    private static final Pattern LOG_FORGING_CHARS = Pattern.compile("[\\r\\n\\p{Cntrl}]");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestId = getOrGenerateRequestId(httpRequest);
        RequestContext.setRequestId(requestId);
        MDC.put(REQUEST_ID_KEY, sanitizeForLog(requestId));

        long startTime = System.currentTimeMillis();

        try {
            logIncomingRequest(httpRequest);
            filterChain.doFilter(httpRequest, httpResponse);
        } finally {
            String userId = extractUserIdFromSecurityContext();
            long duration = System.currentTimeMillis() - startTime;

            RequestContext.setUserId(userId);
            MDC.put(USER_ID_KEY, sanitizeForLog(userId));

            logOutgoingResponse(httpResponse, httpRequest, duration);
            RequestContext.clear();
            MDC.clear();
        }
    }

    private String sanitizeForLog(String value) {
        if (value == null) {
            return null;
        }
        return LOG_FORGING_CHARS.matcher(value).replaceAll("_");
    }

    private String getOrGenerateRequestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }

    private String extractUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "anonymous";
    }

    private void logIncomingRequest(HttpServletRequest request) {
        LOGGER.info("INCOMING_REQUEST - Method: {} Path: {} RemoteAddr: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr()
        );
    }

    private void logOutgoingResponse(HttpServletResponse response, HttpServletRequest request, long duration) {
        LOGGER.info("OUTGOING_RESPONSE - Method: {} Path: {} Status: {} Duration: {}ms",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration
        );
    }
}
