package com.fhlbdm.eadvantage.exception;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fhlbdm.eadvantage.util.RequestContext;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final String HTTP_GET = "GET";
    private static final String MISSING_PATH = "/missing";
    private static final String BOOM_URI = "uri=/api/boom";
    private static final String BOOM_MESSAGE = "boom";

    @Mock
    private WebRequest webRequest;

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @AfterEach
    void tearDown() {
        RequestContext.clear();
    }

    @Test
    @DisplayName("Should return 404 status for a NoHandlerFoundException")
    void shouldReturnNotFoundHttpStatusForNoHandlerFoundException() {
        NoHandlerFoundException ex = new NoHandlerFoundException(HTTP_GET, MISSING_PATH, new HttpHeaders());

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex, webRequest);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "response status should be 404 Not Found");
    }

    @Test
    @DisplayName("Should set the numeric 404 status in the response body")
    void shouldSetNotFoundStatusCodeInResponseBody() {
        NoHandlerFoundException ex = new NoHandlerFoundException(HTTP_GET, MISSING_PATH, new HttpHeaders());

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex, webRequest);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().get("status"),
                "response body status field should be the numeric 404 code");
    }

    @Test
    @DisplayName("Should set a 'Resource not found' message in the response body")
    void shouldSetResourceNotFoundMessageInResponseBody() {
        NoHandlerFoundException ex = new NoHandlerFoundException(HTTP_GET, MISSING_PATH, new HttpHeaders());

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex, webRequest);

        Assertions.assertEquals("Resource not found", response.getBody().get("message"),
                "response body message should describe a missing resource");
    }

    @Test
    @DisplayName("Should use the exception's request URL as the response body path")
    void shouldSetRequestUrlAsPathInNotFoundResponseBody() {
        NoHandlerFoundException ex = new NoHandlerFoundException(HTTP_GET, MISSING_PATH, new HttpHeaders());

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex, webRequest);

        Assertions.assertEquals("/missing", response.getBody().get("path"),
                "response body path should be the unmatched request URL");
    }

    @Test
    @DisplayName("Should include the current request id in the not-found response body")
    void shouldIncludeRequestIdInNotFoundResponseBody() {
        RequestContext.setRequestId("req-404");
        NoHandlerFoundException ex = new NoHandlerFoundException(HTTP_GET, MISSING_PATH, new HttpHeaders());

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex, webRequest);

        Assertions.assertEquals("req-404", response.getBody().get("requestId"),
                "response body requestId should come from RequestContext");
    }

    @Test
    @DisplayName("Should include the current user id in the not-found response body")
    void shouldIncludeUserIdInNotFoundResponseBody() {
        RequestContext.setUserId("user-404");
        NoHandlerFoundException ex = new NoHandlerFoundException(HTTP_GET, MISSING_PATH, new HttpHeaders());

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex, webRequest);

        Assertions.assertEquals("user-404", response.getBody().get("userId"),
                "response body userId should come from RequestContext");
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error for a non-security exception")
    void shouldReturnInternalServerErrorStatusForGenericException() {
        Mockito.when(webRequest.getDescription(false)).thenReturn(BOOM_URI);
        Exception ex = new RuntimeException(BOOM_MESSAGE);

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "unrecognized exceptions should map to 500 Internal Server Error");
    }

    @Test
    @DisplayName("Should return the status carried by a SecurityException with BAD_REQUEST")
    void shouldReturnStatusFromSecurityExceptionWithBadRequestStatus() {
        Mockito.when(webRequest.getDescription(false)).thenReturn("uri=/api/bad");
        SecurityException ex = new SecurityException("invalid", HttpStatus.BAD_REQUEST);

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "response status should come from the SecurityException's status");
    }

    @Test
    @DisplayName("Should return the status carried by a SecurityException with UNAUTHORIZED")
    void shouldReturnStatusFromSecurityExceptionWithUnauthorizedStatus() {
        Mockito.when(webRequest.getDescription(false)).thenReturn("uri=/api/unauthorized");
        SecurityException ex = new SecurityException("no token", HttpStatus.UNAUTHORIZED);

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "response status should come from the SecurityException's status");
    }

    @Test
    @DisplayName("Should return the status carried by a SecurityException with FORBIDDEN")
    void shouldReturnStatusFromSecurityExceptionWithForbiddenStatus() {
        Mockito.when(webRequest.getDescription(false)).thenReturn("uri=/api/forbidden");
        SecurityException ex = new SecurityException("denied", HttpStatus.FORBIDDEN);

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(),
                "response status should come from the SecurityException's status");
    }

    @Test
    @DisplayName("Should return the status carried by a SecurityException with a status outside the logged categories")
    void shouldReturnStatusFromSecurityExceptionWithUnloggedStatus() {
        Mockito.when(webRequest.getDescription(false)).thenReturn("uri=/api/conflict");
        SecurityException ex = new SecurityException("conflict", HttpStatus.CONFLICT);

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode(),
                "response status should come from the SecurityException's status even when it isn't one of the specifically logged statuses");
    }

    @Test
    @DisplayName("Should use the exception's message as the response body message when present")
    void shouldUseExceptionMessageAsResponseMessageWhenPresent() {
        Mockito.when(webRequest.getDescription(false)).thenReturn(BOOM_URI);
        Exception ex = new RuntimeException("custom failure message");

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals("custom failure message", response.getBody().get("message"),
                "response body message should be the exception's own message when present");
    }

    @Test
    @DisplayName("Should fall back to the status reason phrase when the exception has no message")
    void shouldUseReasonPhraseAsResponseMessageWhenExceptionMessageIsNull() {
        Mockito.when(webRequest.getDescription(false)).thenReturn(BOOM_URI);
        Exception ex = new RuntimeException();

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), response.getBody().get("message"),
                "response body message should fall back to the status reason phrase when no exception message exists");
    }

    @Test
    @DisplayName("Should strip the 'uri=' prefix from the WebRequest description for the response path")
    void shouldStripUriPrefixFromPathInResponseBody() {
        Mockito.when(webRequest.getDescription(false)).thenReturn("uri=/api/orders/42");
        Exception ex = new RuntimeException(BOOM_MESSAGE);

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals("/api/orders/42", response.getBody().get("path"),
                "response body path should have the 'uri=' prefix stripped");
    }

    @Test
    @DisplayName("Should include the current request id in the generic exception response body")
    void shouldIncludeRequestIdInGenericExceptionResponseBody() {
        Mockito.when(webRequest.getDescription(false)).thenReturn(BOOM_URI);
        RequestContext.setRequestId("req-500");
        Exception ex = new RuntimeException(BOOM_MESSAGE);

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals("req-500", response.getBody().get("requestId"),
                "response body requestId should come from RequestContext");
    }

    @Test
    @DisplayName("Should include the current user id in the generic exception response body")
    void shouldIncludeUserIdInGenericExceptionResponseBody() {
        Mockito.when(webRequest.getDescription(false)).thenReturn(BOOM_URI);
        RequestContext.setUserId("user-500");
        Exception ex = new RuntimeException(BOOM_MESSAGE);

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals("user-500", response.getBody().get("userId"),
                "response body userId should come from RequestContext");
    }

    @Test
    @DisplayName("Should set the numeric status value in the generic exception response body")
    void shouldSetStatusCodeValueInGenericExceptionResponseBody() {
        Mockito.when(webRequest.getDescription(false)).thenReturn(BOOM_URI);
        Exception ex = new RuntimeException(BOOM_MESSAGE);

        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex, webRequest);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().get("status"),
                "response body status field should be the numeric 500 code");
    }
}
