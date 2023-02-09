package com.hrProject.HR.Project.service;

import com.hrProject.HR.Project.enums.TokenStatus;
import com.hrProject.HR.Project.model.MobileClient;
import com.hrProject.HR.Project.model.VerificationToken;
import com.hrProject.HR.Project.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class VerificationTokenService {


    private final VerificationTokenRepository tokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(VerificationTokenService.class);


    public VerificationTokenService(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public VerificationToken createVerificationToken(MobileClient mobileClient, String token) {
        VerificationToken myToken = new VerificationToken(token, mobileClient);
        return tokenRepository.save(myToken);
    }

    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).orElse(null);
    }

    public TokenStatus validateVerificationToken (VerificationToken verificationToken){
        if (verificationToken == null) {
            logger.error("Verification token is null");
            return TokenStatus.EMPTY;
        }

        MobileClient mobileClient = verificationToken.getMobileClient();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            logger.error("Verification token is expired.");
            return TokenStatus.EXPIRED;

        }
        return TokenStatus.VALID;
    }

}
