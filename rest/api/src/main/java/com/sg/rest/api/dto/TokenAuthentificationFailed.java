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
public class TokenAuthentificationFailed {

    /**
     * @return the reason
     */
    public Reason getReason() {
        return reason;
    }

    public enum Reason {
        NO_TOKEN,
        BAD_TOKEN,
        TOKEN_EXPIRED,
        ACCOUNT_NOT_FOUND,
        NO_CREDENTIALS;
    }
    
    private final Reason reason;
    
    public TokenAuthentificationFailed(Reason reason)
    {
        this.reason = reason;
    }
    
}
