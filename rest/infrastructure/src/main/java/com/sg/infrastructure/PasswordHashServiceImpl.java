/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.infrastructure;

import com.sg.domain.services.PasswordHashService;
import com.sg.domain.vo.PasswordHash;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class PasswordHashServiceImpl implements PasswordHashService {

    private final static Object lock = new Object();
    private static MessageDigest md;

    @Override
    public PasswordHash getHash(String password) {
        byte[] passwordBytes;
        try {
            passwordBytes = password.getBytes("UTF-8");
            synchronized (lock) {
                if (md == null) {
                    md = MessageDigest.getInstance("MD5");
                }
                byte[] digestBytes = md.digest(passwordBytes);
                return new PasswordHash(Base64.getEncoder().encodeToString(digestBytes));
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

}
