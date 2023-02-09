package com.hrProject.HR.Project.utils;


import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Base32;

import java.security.SecureRandom;

@Service
public class SecretGenerator {

    private final SecureRandom randomBytes = new SecureRandom();
    private final static Base32 encoder = new Base32();
    private final int numCharacters;

    public SecretGenerator() {
        this.numCharacters = 32;
    }

    public String generate() {
        return new String(encoder.encode(getRandomBytes()));
    }
    private byte[] getRandomBytes() {
        // 5 bits per char in base32
        byte[] bytes = new byte[(numCharacters * 5) / 8];
        randomBytes.nextBytes(bytes);

        return bytes;
    }
}