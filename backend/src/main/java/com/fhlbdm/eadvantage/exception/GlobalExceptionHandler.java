package com.fhlbdm.eadvantage.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fhlbdm.eadvantage.util.RequestContext;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoHandlerFoundException ex, WebRequest request) {
        String requestId = RequestContext.getRequestId();
        String userId = RequestContext.getUserId();

        LOGGER.warn("404 Not Found - RequestId: {} UserId: {} Path: {}", requestId, userId, ex.getRequestURL());

        Map<String, Object> errorResponse = buildErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                ex.getRequestURL(),
                requestId,
                userId
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, WebRequest request) {
        String requestId = RequestContext.getRequestId();
        String userId = RequestContext.getUserId();

        HttpStatus status = getStatusCode(ex);

        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            LOGGER.error("500 Internal Server Error - RequestId: {} UserId: {}", requestId, userId, ex);
        } else if (status == HttpStatus.BAD_REQUEST) {
            LOGGER.warn("400 Bad Request - RequestId: {} UserId: {} Message: {}", requestId, userId, ex.getMessage());
        } else if (status == HttpStatus.UNAUTHORIZED) {
            LOGGER.warn("401 Unauthorized - RequestId: {} UserId: {}", requestId, userId);
        } else if (status == HttpStatus.FORBIDDEN) {
            LOGGER.warn("403 Forbidden - RequestId: {} UserId: {}", requestId, userId);
        }

        Map<String, Object> errorResponse = buildErrorResponse(
                status.value(),
                ex.getMessage() != null ? ex.getMessage() : status.getReasonPhrase(),
                request.getDescription(false).replace("uri=", ""),
                requestId,
                userId
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    private HttpStatus getStatusCode(Exception ex) {
        if (ex instanceof SecurityException) {
            return ((SecurityException) ex).getStatus();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private Map<String, Object> buildErrorResponse(int status, String message, String path, String requestId, String userId) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status);
        errorResponse.put("message", message);
        errorResponse.put("path", path);
        errorResponse.put("requestId", requestId);
        errorResponse.put("userId", userId);

        return errorResponse;
    }
}
