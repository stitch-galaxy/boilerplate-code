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
public class EmailAccount {

    private final Email email;
    private final Password password;
    private final boolean verified;

    public EmailAccount(
            Email email,
            Password password,
            boolean verified
    ) {
        this.email = email;
        this.password = password;
        this.verified = verified;
    }
}
