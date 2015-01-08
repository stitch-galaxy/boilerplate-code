/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import java.util.Random;

/**
 *
 * @author tarasev
 */
public class SiteAccount {

    private final String email;
    private final String password;
    private final Boolean emailVerified;
    
    private final static Random rnd = new Random();
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public SiteAccount(String email, String password, Boolean emailVerified) {
        this.email = email;
        this.password = password;
        this.emailVerified = emailVerified;
        verifyData();
    }
    
    private void verifyData()
    {
        if (email == null
                || password == null
                || emailVerified == null
                || email.isEmpty()
                || password.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public SiteAccount changePassword(String newPassword) {
        if (!emailVerified) {
            throw new IllegalStateException();
        }
        return new SiteAccount(email, newPassword, emailVerified);
    }

    String randomPassword(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public SiteAccount resetPassword() {
        if (!emailVerified) {
            throw new IllegalStateException();
        }
        return new SiteAccount(email, randomPassword(6), emailVerified);
    }
}
