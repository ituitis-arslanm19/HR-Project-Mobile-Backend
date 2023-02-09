package com.hrProject.HR.Project.repository;

import com.hrProject.HR.Project.model.MobileClient;
import com.hrProject.HR.Project.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByMobileClient(MobileClient mobileClient);
}