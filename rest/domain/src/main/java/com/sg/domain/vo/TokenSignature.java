/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.vo;

import java.time.Instant;

/**
 *
 * @author Admin
 */
public class TokenSignature {
    
    private final String token;
    private final Instant expiresAt;
    
    public TokenSignature(String token,
                            Instant expiresAt)
    {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the expiresAt
     */
    public Instant getExpiresAt() {
        return expiresAt;
    }
}
