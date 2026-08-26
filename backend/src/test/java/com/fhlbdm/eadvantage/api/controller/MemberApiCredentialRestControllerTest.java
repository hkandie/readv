package com.fhlbdm.eadvantage.api.controller;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fhlbdm.eadvantage.api.models.ClientCredentialResponse;
import com.fhlbdm.eadvantage.api.models.ClientCredentialStatus;
import com.fhlbdm.eadvantage.api.models.CreateClientCredentialResponse;
import com.fhlbdm.eadvantage.api.service.MemberApiCredentialService;

@ExtendWith(MockitoExtension.class)
class MemberApiCredentialRestControllerTest {

    private static final String CLIENT_ID = "11111111-1111-1111-1111-111111111111";
    private static final String PLAINTEXT_KEY = "plaintext-key";
    private static final String ACTOR = "tester";
    private static final Integer MEMBER_ID = 42;

    @Mock
    private MemberApiCredentialService service;

    private MemberApiCredentialRestController subject;

    @BeforeEach
    void setUp() {
        subject = new MemberApiCredentialRestController(service);
    }

    private CreateClientCredentialResponse createResponse() {
        return new CreateClientCredentialResponse(
                CLIENT_ID, PLAINTEXT_KEY, ClientCredentialStatus.ACTIVE, ACTOR, LocalDateTime.now());
    }

    @Test
    @DisplayName("Should return 201 Created with the client id and client key on create")
    void shouldReturnCreatedWithClientCredentialOnCreate() {
        Mockito.when(service.createClientCredential(MEMBER_ID)).thenReturn(createResponse());

        ResponseEntity<CreateClientCredentialResponse> response = subject.createClientCredential(MEMBER_ID);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode(),
                "creating a client credential should return 201 Created");
    }

    @Test
    @DisplayName("Should include the generated client key in the create response body")
    void shouldIncludeClientKeyInCreateResponseBody() {
        Mockito.when(service.createClientCredential(MEMBER_ID)).thenReturn(createResponse());

        ResponseEntity<CreateClientCredentialResponse> response = subject.createClientCredential(MEMBER_ID);

        Assertions.assertEquals(PLAINTEXT_KEY, response.getBody().clientKey(),
                "the create response body should include the plaintext client key");
    }

    @Test
    @DisplayName("Should set the Location header to the new credential's resource path")
    void shouldSetLocationHeaderOnCreate() {
        Mockito.when(service.createClientCredential(MEMBER_ID)).thenReturn(createResponse());

        ResponseEntity<CreateClientCredentialResponse> response = subject.createClientCredential(MEMBER_ID);

        Assertions.assertEquals("/api/management/credentials/" + MEMBER_ID,
                response.getHeaders().getLocation().toString(),
                "the Location header should point at the new credential's resource path");
    }

    @Test
    @DisplayName("Should return 200 OK with the credential details on fetch")
    void shouldReturnOkWithCredentialOnFetch() {
        ClientCredentialResponse serviceResponse = new ClientCredentialResponse(
                CLIENT_ID, ClientCredentialStatus.ACTIVE, ACTOR, LocalDateTime.now(), null, null);
        Mockito.when(service.getClientCredential(MEMBER_ID)).thenReturn(serviceResponse);

        ResponseEntity<ClientCredentialResponse> response = subject.getClientCredential(MEMBER_ID);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
                "fetching an existing client credential should return 200 OK");
    }

    @Test
    @DisplayName("Should return the member's client id in the fetch response body")
    void shouldReturnClientIdInFetchResponseBody() {
        ClientCredentialResponse serviceResponse = new ClientCredentialResponse(
                CLIENT_ID, ClientCredentialStatus.ACTIVE, ACTOR, LocalDateTime.now(), null, null);
        Mockito.when(service.getClientCredential(MEMBER_ID)).thenReturn(serviceResponse);

        ResponseEntity<ClientCredentialResponse> response = subject.getClientCredential(MEMBER_ID);

        Assertions.assertEquals(CLIENT_ID, response.getBody().clientId(),
                "the fetch response body should carry the member's client id");
    }

    @Test
    @DisplayName("Should return 204 No Content on successful revoke")
    void shouldReturnNoContentOnRevoke() {
        ResponseEntity<Void> response = subject.revokeClientCredential(MEMBER_ID);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(),
                "revoking a client credential should return 204 No Content");
    }

    @Test
    @DisplayName("Should delegate revocation to the service with the given member id")
    void shouldDelegateRevokeToService() {
        subject.revokeClientCredential(MEMBER_ID);

        Mockito.verify(service).revokeClientCredential(MEMBER_ID);
    }
}
