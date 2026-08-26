package com.fhlbdm.eadvantage.api.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client_credential")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "client_id", nullable = false, unique = true, updatable = false, length = 64)
    private String clientId;

    @Column(name = "client_key_hash", nullable = false)
    private String clientKeyHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClientCredentialStatus status;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "revoked_by")
    private String revokedBy;

    @Column(name = "revoked_on")
    private LocalDateTime revokedOn;
}
