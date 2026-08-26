package com.fhlbdm.eadvantage.api.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fhlbdm.eadvantage.api.models.ClientCredential;
import com.fhlbdm.eadvantage.api.models.ClientCredentialResponse;
import com.fhlbdm.eadvantage.api.models.ClientCredentialStatus;
import com.fhlbdm.eadvantage.api.models.CreateClientCredentialResponse;
import com.fhlbdm.eadvantage.api.repository.MemberApiCredentialRepository;
import com.fhlbdm.eadvantage.exception.SecurityException;
import com.fhlbdm.eadvantage.util.RequestContext;

@Service
public class MemberApiCredentialService {

    private static final int CLIENT_KEY_BYTES = 32;

    private final MemberApiCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public MemberApiCredentialService(MemberApiCredentialRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public CreateClientCredentialResponse createClientCredential(Integer memberId) {
        String clientId = UUID.randomUUID().toString();
        String clientKey = generateClientKey();
        String actor = RequestContext.getUserId();
        LocalDateTime now = LocalDateTime.now();

        ClientCredential credential = ClientCredential.builder()
                .memberId(memberId)
                .clientId(clientId)
                .clientKeyHash(passwordEncoder.encode(clientKey))
                .status(ClientCredentialStatus.ACTIVE)
                .createdBy(actor)
                .createdOn(now)
                .build();

        repository.save(credential);

        return new CreateClientCredentialResponse(
                credential.getClientId(),
                clientKey,
                credential.getStatus(),
                credential.getCreatedBy(),
                credential.getCreatedOn());
    }

    public ClientCredentialResponse getClientCredential(Integer memberId) {
        return toResponse(findByMemberId(memberId));
    }

    public void revokeClientCredential(Integer memberId) {
        ClientCredential credential = findByMemberId(memberId);

        if (credential.getStatus() == ClientCredentialStatus.REVOKED) {
            throw new SecurityException("Client credential is already revoked", HttpStatus.CONFLICT);
        }

        credential.setStatus(ClientCredentialStatus.REVOKED);
        credential.setRevokedBy(RequestContext.getUserId());
        credential.setRevokedOn(LocalDateTime.now());
        repository.save(credential);
    }

    private ClientCredential findByMemberId(Integer memberId) {
        return repository.findByMemberId(memberId)
                .orElseThrow(() -> new SecurityException("Client credential not found", HttpStatus.NOT_FOUND));
    }

    private ClientCredentialResponse toResponse(ClientCredential credential) {
        return new ClientCredentialResponse(
                credential.getClientId(),
                credential.getStatus(),
                credential.getCreatedBy(),
                credential.getCreatedOn(),
                credential.getRevokedBy(),
                credential.getRevokedOn());
    }

    private String generateClientKey() {
        byte[] buffer = new byte[CLIENT_KEY_BYTES];
        secureRandom.nextBytes(buffer);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer);
    }
}
