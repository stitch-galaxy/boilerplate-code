/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.vo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *
 * @author Admin
 */
public class Password {

    private final String hash;

    private final static Object lock = new Object();
    private static MessageDigest md;

    public static Password buildPassword(String password) {
        return new Password(buildHash(password));
    }

    public Password(String hash) {
        this.hash = hash;
    }

    public boolean verify(String mayBePwd) {
        return hash.equals(buildHash(mayBePwd));
    }

    private static String buildHash(String password) {
        byte[] passwordBytes;
        try {
            passwordBytes = password.getBytes("UTF-8");
            synchronized (lock) {
                if (md == null) {
                    md = MessageDigest.getInstance("MD5");
                }
                byte[] digestBytes = md.digest(passwordBytes);
                return Base64.getEncoder().encodeToString(digestBytes);
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
