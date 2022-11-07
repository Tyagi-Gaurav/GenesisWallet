package com.gw.security.util;

public interface PasswordEncryptor {
    String encrypt(String password, String salt);
}
