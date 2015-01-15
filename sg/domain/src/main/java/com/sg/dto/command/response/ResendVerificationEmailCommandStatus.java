/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.command.response;

import com.sg.dto.enumerations.ResendVerificationEmailStatus;

/**
 *
 * @author tarasev
 */
public class ResendVerificationEmailCommandStatus implements CommandResponse {

    private final ResendVerificationEmailStatus status;

    public ResendVerificationEmailCommandStatus(ResendVerificationEmailStatus status) {
        if (status == null) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }

    /**
     * @return the status
     */
    public ResendVerificationEmailStatus getStatus() {
        return status;
    }
}
