/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.command.response.cqrs;

import com.sg.dto.enumerations.SigninStatus;

/**
 *
 * @author tarasev
 */
public class SigninCommandResponse implements CommandResponse {

    private final SigninStatus status;
    private final String token;

    public SigninCommandResponse(SigninStatus status) {
        if (status == null || status == SigninStatus.STATUS_SUCCESS) {
            throw new IllegalArgumentException();
        }
        this.status = status;
        this.token = null;
    }

    public SigninCommandResponse(String token) {
        if (token == null)
            throw new IllegalArgumentException();
        this.status = SigninStatus.STATUS_SUCCESS;
        this.token = token;
    }
    
    /**
     * @return the status
     */
    public SigninStatus getStatus() {
        return status;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }
}
