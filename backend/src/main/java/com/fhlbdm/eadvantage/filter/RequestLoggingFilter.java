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

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID_MDC_KEY = "requestId";
    private static final String USER_ID_MDC_KEY = "userId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestId = getOrGenerateRequestId(httpRequest);
        RequestContext.setRequestId(requestId);
        MDC.put(REQUEST_ID_MDC_KEY, requestId);

        long startTime = System.currentTimeMillis();

        try {
            logIncomingRequest(httpRequest, requestId);
            filterChain.doFilter(httpRequest, httpResponse);
        } finally {
            String userId = extractUserIdFromSecurityContext();
            long duration = System.currentTimeMillis() - startTime;

            RequestContext.setUserId(userId);
            MDC.put(USER_ID_MDC_KEY, userId);

            logOutgoingResponse(httpResponse, httpRequest, requestId, userId, duration);
            RequestContext.clear();
            MDC.clear();
        }
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

    private void logIncomingRequest(HttpServletRequest request, String requestId) {
        logger.info("INCOMING_REQUEST - Method: {} Path: {} RemoteAddr: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr()
        );
    }

    private void logOutgoingResponse(HttpServletResponse response, HttpServletRequest request, String requestId, String userId, long duration) {
        logger.info("OUTGOING_RESPONSE - Method: {} Path: {} Status: {} Duration: {}ms",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration
        );
    }
}
