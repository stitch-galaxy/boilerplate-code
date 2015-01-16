/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.operation.response;

import com.sg.dto.operation.status.SignupStatus;

/**
 *
 * @author tarasev
 */
public class SignupCommandResponse implements Response {

    private final SignupStatus status;

    public SignupCommandResponse(SignupStatus status) {
        if (status == null) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }

    /**
     * @return the status
     */
    public SignupStatus getStatus() {
        return status;
    }
}
