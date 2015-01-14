/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.command.response.cqrs;

import com.sg.dto.enumerations.CompleteSignupCommandStatus;

/**
 *
 * @author tarasev
 */
public class CompleteSignupCommandResponse implements CommandResponse {

    private final CompleteSignupCommandStatus status;
    private final String token;

    public CompleteSignupCommandResponse(CompleteSignupCommandStatus status) {
        if (status == null || status == CompleteSignupCommandStatus.STATUS_SUCCESS) {
            throw new IllegalArgumentException();
        }
        this.status = status;
        this.token = null;
    }

    public CompleteSignupCommandResponse(String token) {
        this.status = CompleteSignupCommandStatus.STATUS_SUCCESS;
        this.token = token;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

}
