package com.gw.security.util;

import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordEncryptorImpl implements PasswordEncryptor {
    private final MessageDigest messageDigest;

    public PasswordEncryptorImpl(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }

    public String encrypt(String password, String salt) {
        byte[] digest = messageDigest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(digest));
    }
}
