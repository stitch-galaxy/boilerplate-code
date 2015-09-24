/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api.dto;

/**
 *
 * @author Admin
 */
public class TokenInfo {

    private final String token;
    private final long expiresIn;

    public TokenInfo(String token,
                                   long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the expiresIn
     */
    public long getExpiresIn() {
        return expiresIn;
    }
}
