package com.fhlbdm.eadvantage.web.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(SpaForwardController.class)
class SpaForwardControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Test
    void forwardsSingleSegmentClientRouteToIndexHtml() {
        mockMvc.get().uri("/dashboard")
                .assertThat()
                .hasForwardedUrl("/index.html");
    }

    @Test
    void forwardsMultiSegmentClientRouteToIndexHtml() {
        mockMvc.get().uri("/settings/profile")
                .assertThat()
                .hasForwardedUrl("/index.html");
    }

    @Test
    void doesNotInterceptDottedStaticAssetPaths() {
        mockMvc.get().uri("/favicon.svg")
                .assertThat()
                .hasForwardedUrl(null);
    }
}
