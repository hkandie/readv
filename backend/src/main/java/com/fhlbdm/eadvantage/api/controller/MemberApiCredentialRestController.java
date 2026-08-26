package com.fhlbdm.eadvantage.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.fhlbdm.eadvantage.api.models.ClientCredentialResponse;
import com.fhlbdm.eadvantage.api.models.CreateClientCredentialResponse;
import com.fhlbdm.eadvantage.api.service.MemberApiCredentialService;

@RestController
@RequestMapping("/api/management/credentials")
public class MemberApiCredentialRestController {

    private final MemberApiCredentialService service;

    public MemberApiCredentialRestController(MemberApiCredentialService service) {
        this.service = service;
    }

    @PostMapping("/{memberId}")
    public ResponseEntity<CreateClientCredentialResponse> createClientCredential(
            @PathVariable Integer memberId) {
        CreateClientCredentialResponse response = service.createClientCredential(memberId);

        URI location = UriComponentsBuilder.fromPath("/api/management/credentials/{memberId}")
                .buildAndExpand(memberId)
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ClientCredentialResponse> getClientCredential(@PathVariable Integer memberId) {
        return ResponseEntity.ok(service.getClientCredential(memberId));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> revokeClientCredential(@PathVariable Integer memberId) {
        service.revokeClientCredential(memberId);
        return ResponseEntity.noContent().build();
    }
}
