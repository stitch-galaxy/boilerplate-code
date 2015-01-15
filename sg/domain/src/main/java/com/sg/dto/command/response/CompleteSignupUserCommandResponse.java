/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.command.response;

import com.sg.dto.enumerations.CompleteSignupCommandStatus;

/**
 *
 * @author tarasev
 */
public class CompleteSignupUserCommandResponse {

    private final CompleteSignupCommandStatus status;

    public CompleteSignupUserCommandResponse(CompleteSignupCommandStatus status) {
        this.status = status;
    }

    /**
     * @return the status
     */
    public CompleteSignupCommandStatus getStatus() {
        return status;
    }

}
