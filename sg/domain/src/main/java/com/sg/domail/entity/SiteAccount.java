/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.entity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author tarasev
 */
public class SiteAccount {

    private final String email;
    private String passwordHash;
    private boolean emailVerified;
    private final MessageDigest md;

    public SiteAccount(String email, String password) {
        try {
            this.md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new PlatformDoNotSupportMd5AlgorithmException(ex);
        }
        if (email == null
                || password == null
                || email.isEmpty()
                || password.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.email = email;
        this.passwordHash = getPasswordHash(password);
        this.emailVerified = false;
    }

    private String getPasswordHash(String password) {
        try {
            byte[] passwordBytes = password.getBytes("UTF-8");
            byte[] digestBytes = md.digest(passwordBytes);
            return Base64.encodeBase64String(digestBytes);
        } catch (UnsupportedEncodingException ex) {
            throw new PlatformDoNotSupportUtf8AlgorithmException(ex);
        }
    }

    public void changePassword(String newPassword) {
        if (!isEmailVerified()) {
            throw new IllegalStateException();
        }
        this.passwordHash = getPasswordHash(newPassword);
    }

    public boolean isPasswordCorrect(String passwordToVerify) {
        if (passwordToVerify == null)
            throw new IllegalArgumentException();
        String passwordToVerifyHash = getPasswordHash(passwordToVerify);
        return this.passwordHash.equals(passwordToVerifyHash);
    }

    /**
     * @return the emailVerified
     */
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void verifyEmail() {
        if (emailVerified) {
            throw new IllegalStateException();
        }
        emailVerified = true;
    }

    private static class PlatformDoNotSupportMd5AlgorithmException extends RuntimeException {

        public PlatformDoNotSupportMd5AlgorithmException(NoSuchAlgorithmException ex) {
            super(ex);
        }
    }

    private static class PlatformDoNotSupportUtf8AlgorithmException extends RuntimeException {

        public PlatformDoNotSupportUtf8AlgorithmException(UnsupportedEncodingException ex) {
            super(ex);
        }
    }
}
