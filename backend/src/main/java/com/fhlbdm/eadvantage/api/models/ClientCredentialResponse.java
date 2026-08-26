package com.fhlbdm.eadvantage.api.models;

import java.time.LocalDateTime;

public record ClientCredentialResponse(
        String clientId,
        ClientCredentialStatus status,
        String createdBy,
        LocalDateTime createdOn,
        String revokedBy,
        LocalDateTime revokedOn) {
}
