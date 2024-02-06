package com.gw.security.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordEncryptorTest {

    private PasswordEncryptor passwordEncryptor;

    @Mock
    private MessageDigest messageDigest;

    @BeforeEach
    void setUp() {
        passwordEncryptor = new PasswordEncryptorImpl(messageDigest);
    }

    @Test
    void shouldEncodeString() {
        String salt = RandomStringUtils.randomAlphanumeric(10);
        String stringToEncode = "stringToEncode";
        String expectedEncodedString = "encodedString";
        String expectedHex = "656e636f646564537472696e67";

        when(messageDigest.digest((stringToEncode + salt).getBytes(StandardCharsets.UTF_8)))
                .thenReturn(expectedEncodedString.getBytes(StandardCharsets.UTF_8));

        String encodedString = passwordEncryptor.encrypt(stringToEncode, salt);

        assertThat(encodedString).isEqualTo(expectedHex);
    }
}