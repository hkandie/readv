package com.fhlbdm.eadvantage.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fhlbdm.eadvantage.util.RequestContext;

import java.io.IOException;
import java.util.regex.Pattern;

@ExtendWith(MockitoExtension.class)
class RequestLoggingFilterTest {

    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private static final String JWS_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSIsInJvbGUiOiJhZG1pbiJ9.dGhpc19pc19hX3NpZ25hdHVyZQ";

    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private final RequestLoggingFilter filter = new RequestLoggingFilter();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        RequestContext.clear();
        MDC.clear();
    }

    @Test
    @DisplayName("Should use the request id from the X-Request-ID header when present")
    void shouldUseRequestIdFromHeaderWhenPresent() throws Exception {
        Mockito.when(request.getHeader(REQUEST_ID_HEADER)).thenReturn("client-req-id");
        String[] capturedRequestId = new String[1];
        Mockito.doAnswer(invocation -> {
            capturedRequestId[0] = RequestContext.getRequestId();
            return null;
        }).when(filterChain).doFilter(request, response);

        filter.doFilter(request, response, filterChain);

        Assertions.assertEquals("client-req-id", capturedRequestId[0],
                "request id from the incoming header should be propagated to RequestContext");
    }

    @Test
    @DisplayName("Should generate a UUID request id when the header is missing")
    void shouldGenerateRequestIdWhenHeaderMissing() throws Exception {
        Mockito.when(request.getHeader(REQUEST_ID_HEADER)).thenReturn(null);
        String[] capturedRequestId = new String[1];
        Mockito.doAnswer(invocation -> {
            capturedRequestId[0] = RequestContext.getRequestId();
            return null;
        }).when(filterChain).doFilter(request, response);

        filter.doFilter(request, response, filterChain);

        Assertions.assertTrue(UUID_PATTERN.matcher(capturedRequestId[0]).matches(),
                "a UUID request id should be generated when the header is missing");
    }

    @Test
    @DisplayName("Should generate a UUID request id when the header is blank")
    void shouldGenerateRequestIdWhenHeaderEmpty() throws Exception {
        Mockito.when(request.getHeader(REQUEST_ID_HEADER)).thenReturn("");
        String[] capturedRequestId = new String[1];
        Mockito.doAnswer(invocation -> {
            capturedRequestId[0] = RequestContext.getRequestId();
            return null;
        }).when(filterChain).doFilter(request, response);

        filter.doFilter(request, response, filterChain);

        Assertions.assertTrue(UUID_PATTERN.matcher(capturedRequestId[0]).matches(),
                "a UUID request id should be generated when the header is blank");
    }

    @Test
    @DisplayName("Should strip CRLF characters from the request id before it enters the MDC")
    void shouldStripCrlfFromRequestIdMdc() throws Exception {
        Mockito.when(request.getHeader(REQUEST_ID_HEADER)).thenReturn("abc\r\nINJECTED - Method: FAKE");
        String[] mdcRequestId = new String[1];
        Mockito.doAnswer(invocation -> {
            mdcRequestId[0] = MDC.get("requestId");
            return null;
        }).when(filterChain).doFilter(request, response);

        filter.doFilter(request, response, filterChain);

        Assertions.assertFalse(mdcRequestId[0].contains("\r") || mdcRequestId[0].contains("\n"),
                "MDC requestId should not contain raw CR/LF characters from the header");
    }

    @Test
    @DisplayName("Should strip CRLF characters from the user id before it enters the MDC")
    void shouldStripCrlfFromUserIdMdc() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("attacker\r\nINJECTED - Status: 200");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String[] capturedMdcUserId = new String[1];
        Mockito.when(response.getStatus()).thenAnswer(invocation -> {
            capturedMdcUserId[0] = MDC.get("userId");
            return 200;
        });

        filter.doFilter(request, response, filterChain);

        Assertions.assertFalse(capturedMdcUserId[0].contains("\r") || capturedMdcUserId[0].contains("\n"),
                "MDC userId should not contain raw CR/LF characters from the authenticated principal name");
    }

    @Test
    @DisplayName("Should invoke the filter chain with the original request and response")
    void shouldInvokeFilterChainWithSameRequestAndResponse() throws Exception {
        filter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should set the user id from the authenticated JWS principal's name")
    void shouldSetUserIdFromAuthenticatedJwsPrincipal() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn(JWS_TOKEN);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String[] capturedUserId = new String[1];
        Mockito.when(response.getStatus()).thenAnswer(invocation -> {
            capturedUserId[0] = RequestContext.getUserId();
            return 200;
        });

        filter.doFilter(request, response, filterChain);

        Assertions.assertEquals(JWS_TOKEN, capturedUserId[0],
                "user id should come from the authenticated JWS principal's name");
    }

    @Test
    @DisplayName("Should set the user id to anonymous when there is no authentication")
    void shouldSetAnonymousUserIdWhenNoAuthenticationPresent() throws Exception {
        String[] capturedUserId = new String[1];
        Mockito.when(response.getStatus()).thenAnswer(invocation -> {
            capturedUserId[0] = RequestContext.getUserId();
            return 401;
        });

        filter.doFilter(request, response, filterChain);

        Assertions.assertEquals("anonymous", capturedUserId[0],
                "user id should default to anonymous when there is no authentication");
    }

    @Test
    @DisplayName("Should set the user id to anonymous when the JWS principal is not authenticated")
    void shouldSetAnonymousUserIdWhenJwsPrincipalNotAuthenticated() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String[] capturedUserId = new String[1];
        Mockito.when(response.getStatus()).thenAnswer(invocation -> {
            capturedUserId[0] = RequestContext.getUserId();
            return 401;
        });

        filter.doFilter(request, response, filterChain);

        Assertions.assertEquals("anonymous", capturedUserId[0],
                "user id should default to anonymous when the JWS principal is not authenticated");
    }

    @Test
    @DisplayName("Should reset the request id to unknown after processing completes")
    void shouldResetRequestIdToUnknownAfterProcessing() throws Exception {
        filter.doFilter(request, response, filterChain);

        Assertions.assertEquals("unknown", RequestContext.getRequestId(),
                "RequestContext request id should be cleared once the filter completes");
    }

    @Test
    @DisplayName("Should reset the user id to anonymous after processing completes")
    void shouldResetUserIdToAnonymousAfterProcessing() throws Exception {
        filter.doFilter(request, response, filterChain);

        Assertions.assertEquals("anonymous", RequestContext.getUserId(),
                "RequestContext user id should be cleared once the filter completes");
    }

    @Test
    @DisplayName("Should clear the MDC request id after processing completes")
    void shouldClearRequestIdMdcAfterProcessing() throws Exception {
        filter.doFilter(request, response, filterChain);

        Assertions.assertNull(MDC.get("requestId"), "MDC requestId should be cleared once the filter completes");
    }

    @Test
    @DisplayName("Should clear the MDC user id after processing completes")
    void shouldClearUserIdMdcAfterProcessing() throws Exception {
        filter.doFilter(request, response, filterChain);

        Assertions.assertNull(MDC.get("userId"), "MDC userId should be cleared once the filter completes");
    }

    @Test
    @DisplayName("Should propagate exceptions thrown by the filter chain")
    void shouldPropagateExceptionThrownByFilterChain() throws IOException, ServletException {
        Mockito.doThrow(new ServletException("boom")).when(filterChain).doFilter(request, response);

        Assertions.assertThrows(ServletException.class,
                () -> filter.doFilter(request, response, filterChain),
                "exceptions from downstream filters/handlers should propagate to the caller");
    }

    @Test
    @DisplayName("Should clear the request id when the filter chain throws")
    void shouldClearRequestIdWhenFilterChainThrows() throws Exception {
        Mockito.doThrow(new ServletException("boom")).when(filterChain).doFilter(request, response);

        try {
            filter.doFilter(request, response, filterChain);
        } catch (ServletException expected) {
            // asserted separately in shouldPropagateExceptionThrownByFilterChain
        }

        Assertions.assertEquals("unknown", RequestContext.getRequestId(),
                "RequestContext should still be cleared when the filter chain throws");
    }

    @Test
    @DisplayName("Should clear the MDC request id when the filter chain throws")
    void shouldClearRequestIdMdcWhenFilterChainThrows() throws Exception {
        Mockito.doThrow(new ServletException("boom")).when(filterChain).doFilter(request, response);

        try {
            filter.doFilter(request, response, filterChain);
        } catch (ServletException expected) {
            // asserted separately in shouldPropagateExceptionThrownByFilterChain
        }

        Assertions.assertNull(MDC.get("requestId"), "MDC should still be cleared when the filter chain throws");
    }
}
