/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.command.response;

import com.sg.dto.enumerations.SignupStatus;

/**
 *
 * @author tarasev
 */
public class SignupCommandStatus implements CommandResponse {

    private final SignupStatus status;

    public SignupCommandStatus(SignupStatus status) {
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
