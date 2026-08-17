package com.fhlbdm.eadvantage;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class ApplicationTest {

    @Test
    void mainShouldStartTheSpringApplicationWithApplicationClassAndArgs() {
        String[] args = {"--server.port=0"};

        try (MockedStatic<SpringApplication> springApplication = Mockito.mockStatic(SpringApplication.class)) {
            springApplication.when(() -> SpringApplication.run(Application.class, args))
                    .thenReturn(Mockito.mock(ConfigurableApplicationContext.class));

            Application.main(args);

            springApplication.verify(() -> SpringApplication.run(Application.class, args));
        }
    }
}
