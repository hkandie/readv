package com.fhlbdm.eadvantage.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestContextTest {

    @AfterEach
    void tearDown() {
        RequestContext.clear();
    }

    @Test
    @DisplayName("Should default the request id to unknown when unset")
    void shouldDefaultRequestIdToUnknown() {
        Assertions.assertEquals("unknown", RequestContext.getRequestId(),
                "request id should default to unknown when never set");
    }

    @Test
    @DisplayName("Should default the user id to anonymous when unset")
    void shouldDefaultUserIdToAnonymous() {
        Assertions.assertEquals("anonymous", RequestContext.getUserId(),
                "user id should default to anonymous when never set");
    }

    @Test
    @DisplayName("Should return the request id that was set")
    void shouldReturnRequestIdThatWasSet() {
        RequestContext.setRequestId("req-1");

        Assertions.assertEquals("req-1", RequestContext.getRequestId(),
                "request id should be the value previously set");
    }

    @Test
    @DisplayName("Should return the user id that was set")
    void shouldReturnUserIdThatWasSet() {
        RequestContext.setUserId("user-1");

        Assertions.assertEquals("user-1", RequestContext.getUserId(),
                "user id should be the value previously set");
    }

    @Test
    @DisplayName("Should allow direct instantiation of the utility class")
    void shouldAllowDirectInstantiation() {
        Assertions.assertNotNull(new RequestContext(), "utility class should be directly instantiable");
    }
}
