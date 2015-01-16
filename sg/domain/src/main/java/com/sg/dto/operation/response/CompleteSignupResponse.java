/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.operation.response;

import com.sg.dto.operation.status.CompleteSignupCommandStatus;

/**
 *
 * @author tarasev
 */
public class CompleteSignupResponse implements Response {

    private final CompleteSignupCommandStatus status;
    private final String token;

    public CompleteSignupResponse(CompleteSignupCommandStatus status) {
        if (status == null || status == CompleteSignupCommandStatus.STATUS_SUCCESS) {
            throw new IllegalArgumentException();
        }
        this.status = status;
        this.token = null;
    }

    public CompleteSignupResponse(String token) {
        this.status = CompleteSignupCommandStatus.STATUS_SUCCESS;
        this.token = token;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the status
     */
    public CompleteSignupCommandStatus getStatus() {
        return status;
    }

}
