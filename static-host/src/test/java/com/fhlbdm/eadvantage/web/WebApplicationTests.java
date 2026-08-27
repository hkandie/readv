package com.fhlbdm.eadvantage.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
class WebApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainStartsTheApplication() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            springApplication.when(() -> SpringApplication.run(eq(WebApplication.class), any(String[].class)))
                    .thenReturn(mock(ConfigurableApplicationContext.class));

            WebApplication.main(new String[] {});

            springApplication.verify(() -> SpringApplication.run(eq(WebApplication.class), any(String[].class)));
        }
    }
}
