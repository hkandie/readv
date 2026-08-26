package com.fhlbdm.eadvantage.api.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fhlbdm.eadvantage.api.models.ClientCredential;
import com.fhlbdm.eadvantage.api.models.ClientCredentialResponse;
import com.fhlbdm.eadvantage.api.models.ClientCredentialStatus;
import com.fhlbdm.eadvantage.api.models.CreateClientCredentialResponse;
import com.fhlbdm.eadvantage.api.repository.MemberApiCredentialRepository;
import com.fhlbdm.eadvantage.exception.SecurityException;
import com.fhlbdm.eadvantage.util.RequestContext;

@ExtendWith(MockitoExtension.class)
class MemberApiCredentialServiceTest {

    private static final String CLIENT_ID = "11111111-1111-1111-1111-111111111111";
    private static final String ACTOR = "tester";
    private static final Integer MEMBER_ID = 42;

    @Mock
    private MemberApiCredentialRepository repository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MemberApiCredentialService service;

    @BeforeEach
    void setUp() {
        service = new MemberApiCredentialService(repository, passwordEncoder);
    }

    @AfterEach
    void tearDown() {
        RequestContext.clear();
    }

    private ClientCredential activeCredential() {
        return ClientCredential.builder()
                .id(1L)
                .memberId(MEMBER_ID)
                .clientId(CLIENT_ID)
                .clientKeyHash("hash")
                .status(ClientCredentialStatus.ACTIVE)
                .createdBy(ACTOR)
                .createdOn(LocalDateTime.now())
                .build();
    }

    private HttpStatus statusFromThrownException(Runnable action) {
        try {
            action.run();
        } catch (SecurityException ex) {
            return ex.getStatus();
        }
        return null;
    }

    @Test
    @DisplayName("Should persist a new credential and return the plaintext client key on create")
    void shouldReturnPlaintextClientKeyOnCreate() {
        RequestContext.setUserId(ACTOR);
        Mockito.when(repository.save(Mockito.any(ClientCredential.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateClientCredentialResponse response = service.createClientCredential(MEMBER_ID);

        Assertions.assertNotNull(response.clientKey(), "client key should be returned in plaintext on create");
    }

    @Test
    @DisplayName("Should generate a distinct client id on create")
    void shouldGenerateClientIdOnCreate() {
        RequestContext.setUserId(ACTOR);
        Mockito.when(repository.save(Mockito.any(ClientCredential.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateClientCredentialResponse response = service.createClientCredential(MEMBER_ID);

        Assertions.assertNotNull(response.clientId(), "a client id should be generated on create");
    }

    @Test
    @DisplayName("Should persist the client key as a hash, not the plaintext value")
    void shouldPersistHashedClientKeyOnCreate() {
        RequestContext.setUserId(ACTOR);
        ArgumentCaptor<ClientCredential> captor = ArgumentCaptor.forClass(ClientCredential.class);
        Mockito.when(repository.save(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateClientCredentialResponse response = service.createClientCredential(MEMBER_ID);

        Assertions.assertNotEquals(response.clientKey(), captor.getValue().getClientKeyHash(),
                "the persisted client key hash should not equal the plaintext client key");
    }

    @Test
    @DisplayName("Should persist the member id the credential was created for")
    void shouldPersistMemberIdOnCreate() {
        RequestContext.setUserId(ACTOR);
        ArgumentCaptor<ClientCredential> captor = ArgumentCaptor.forClass(ClientCredential.class);
        Mockito.when(repository.save(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.createClientCredential(MEMBER_ID);

        Assertions.assertEquals(MEMBER_ID, captor.getValue().getMemberId(),
                "the persisted credential should carry the member id it was created for");
    }

    @Test
    @DisplayName("Should set the created-by audit field from the request context on create")
    void shouldSetCreatedByFromRequestContextOnCreate() {
        RequestContext.setUserId(ACTOR);
        Mockito.when(repository.save(Mockito.any(ClientCredential.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateClientCredentialResponse response = service.createClientCredential(MEMBER_ID);

        Assertions.assertEquals(ACTOR, response.createdBy(), "createdBy should come from the request context");
    }

    @Test
    @DisplayName("Should default a newly created credential to ACTIVE status")
    void shouldDefaultNewCredentialToActiveStatus() {
        RequestContext.setUserId(ACTOR);
        Mockito.when(repository.save(Mockito.any(ClientCredential.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateClientCredentialResponse response = service.createClientCredential(MEMBER_ID);

        Assertions.assertEquals(ClientCredentialStatus.ACTIVE, response.status(),
                "a newly created credential should be ACTIVE");
    }

    @Test
    @DisplayName("Should return the credential when fetching by an existing member id")
    void shouldReturnCredentialWhenFound() {
        Mockito.when(repository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(activeCredential()));

        ClientCredentialResponse response = service.getClientCredential(MEMBER_ID);

        Assertions.assertEquals(CLIENT_ID, response.clientId(), "response should carry the member's client id");
    }

    @Test
    @DisplayName("Should throw a 404 when fetching by a member id that does not exist")
    void shouldThrowNotFoundWhenFetchingMissingCredential() {
        Mockito.when(repository.findByMemberId(MEMBER_ID)).thenReturn(Optional.empty());

        HttpStatus status = statusFromThrownException(() -> service.getClientCredential(MEMBER_ID));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, status, "missing credential should map to 404");
    }

    @Test
    @DisplayName("Should mark an active credential as revoked with actor and timestamp")
    void shouldRevokeActiveCredential() {
        RequestContext.setUserId(ACTOR);
        ClientCredential credential = activeCredential();
        Mockito.when(repository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(credential));
        Mockito.when(repository.save(Mockito.any(ClientCredential.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.revokeClientCredential(MEMBER_ID);

        Assertions.assertEquals(ClientCredentialStatus.REVOKED, credential.getStatus(),
                "credential status should become REVOKED");
    }

    @Test
    @DisplayName("Should record who revoked the credential")
    void shouldRecordRevokedBy() {
        RequestContext.setUserId(ACTOR);
        ClientCredential credential = activeCredential();
        Mockito.when(repository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(credential));
        Mockito.when(repository.save(Mockito.any(ClientCredential.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.revokeClientCredential(MEMBER_ID);

        Assertions.assertEquals(ACTOR, credential.getRevokedBy(), "revokedBy should come from the request context");
    }

    @Test
    @DisplayName("Should record when the credential was revoked")
    void shouldRecordRevokedOn() {
        RequestContext.setUserId(ACTOR);
        ClientCredential credential = activeCredential();
        Mockito.when(repository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(credential));
        Mockito.when(repository.save(Mockito.any(ClientCredential.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.revokeClientCredential(MEMBER_ID);

        Assertions.assertNotNull(credential.getRevokedOn(), "revokedOn should be set once revoked");
    }

    @Test
    @DisplayName("Should throw a 404 when revoking a member id that does not exist")
    void shouldThrowNotFoundWhenRevokingMissingCredential() {
        Mockito.when(repository.findByMemberId(MEMBER_ID)).thenReturn(Optional.empty());

        HttpStatus status = statusFromThrownException(() -> service.revokeClientCredential(MEMBER_ID));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, status, "missing credential should map to 404");
    }

    @Test
    @DisplayName("Should throw a 409 when revoking an already-revoked credential")
    void shouldThrowConflictWhenRevokingAlreadyRevokedCredential() {
        ClientCredential credential = activeCredential();
        credential.setStatus(ClientCredentialStatus.REVOKED);
        Mockito.when(repository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(credential));

        HttpStatus status = statusFromThrownException(() -> service.revokeClientCredential(MEMBER_ID));

        Assertions.assertEquals(HttpStatus.CONFLICT, status, "already-revoked credential should map to 409");
    }
}
