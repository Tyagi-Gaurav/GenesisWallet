package com.gw.security.config;

import com.gw.security.util.PasswordEncryptor;
import com.gw.security.util.PasswordEncryptorImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
public class EncryptionFactory {
    @Bean
    public PasswordEncryptor dataEncoder(@Qualifier("PasswordEncryptor") MessageDigest messageDigest) {
        return new PasswordEncryptorImpl(messageDigest);
    }

    @Bean
    @Qualifier("PasswordEncryptor")
    public MessageDigest messageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA3-256");
    }

    @Bean
    SecretKeyFactory factory() throws NoSuchAlgorithmException {
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    }
}
