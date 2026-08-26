package com.fhlbdm.eadvantage.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fhlbdm.eadvantage.api.models.ClientCredential;

public interface MemberApiCredentialRepository extends JpaRepository<ClientCredential, Long> {

    Optional<ClientCredential> findByMemberId(Integer memberId);
}
