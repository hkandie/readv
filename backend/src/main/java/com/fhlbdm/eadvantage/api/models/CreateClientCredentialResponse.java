package com.fhlbdm.eadvantage.api.models;

import java.time.LocalDateTime;

public record CreateClientCredentialResponse(
        String clientId,
        String clientKey,
        ClientCredentialStatus status,
        String createdBy,
        LocalDateTime createdOn) {
}
