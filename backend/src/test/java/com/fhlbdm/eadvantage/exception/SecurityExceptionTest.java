package com.fhlbdm.eadvantage.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class SecurityExceptionTest {

    private static final String INVALID_MESSAGE = "invalid";
    private static final String DENIED_MESSAGE = "denied";

    @Test
    @DisplayName("Should carry the message given to the two-argument constructor")
    void shouldCarryMessageFromTwoArgConstructor() {
        SecurityException ex = new SecurityException(INVALID_MESSAGE, HttpStatus.BAD_REQUEST);

        Assertions.assertEquals(INVALID_MESSAGE, ex.getMessage(), "message should come from the constructor argument");
    }

    @Test
    @DisplayName("Should carry the status given to the two-argument constructor")
    void shouldCarryStatusFromTwoArgConstructor() {
        SecurityException ex = new SecurityException(INVALID_MESSAGE, HttpStatus.BAD_REQUEST);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus(), "status should come from the constructor argument");
    }

    @Test
    @DisplayName("Should have no cause when constructed with the two-argument constructor")
    void shouldHaveNoCauseFromTwoArgConstructor() {
        SecurityException ex = new SecurityException(INVALID_MESSAGE, HttpStatus.BAD_REQUEST);

        Assertions.assertNull(ex.getCause(), "cause should be null when not provided");
    }

    @Test
    @DisplayName("Should carry the message given to the three-argument constructor")
    void shouldCarryMessageFromThreeArgConstructor() {
        Throwable cause = new IllegalStateException("root cause");

        SecurityException ex = new SecurityException(DENIED_MESSAGE, HttpStatus.FORBIDDEN, cause);

        Assertions.assertEquals(DENIED_MESSAGE, ex.getMessage(), "message should come from the constructor argument");
    }

    @Test
    @DisplayName("Should carry the status given to the three-argument constructor")
    void shouldCarryStatusFromThreeArgConstructor() {
        Throwable cause = new IllegalStateException("root cause");

        SecurityException ex = new SecurityException(DENIED_MESSAGE, HttpStatus.FORBIDDEN, cause);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, ex.getStatus(), "status should come from the constructor argument");
    }

    @Test
    @DisplayName("Should carry the cause given to the three-argument constructor")
    void shouldCarryCauseFromThreeArgConstructor() {
        Throwable cause = new IllegalStateException("root cause");

        SecurityException ex = new SecurityException(DENIED_MESSAGE, HttpStatus.FORBIDDEN, cause);

        Assertions.assertSame(cause, ex.getCause(), "cause should come from the constructor argument");
    }
}
