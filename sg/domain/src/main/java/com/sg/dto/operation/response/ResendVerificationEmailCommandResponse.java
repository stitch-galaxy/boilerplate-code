/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.operation.response;

import com.sg.dto.operation.status.ResendVerificationEmailStatus;

/**
 *
 * @author tarasev
 */
public class ResendVerificationEmailCommandResponse implements Response {

    private final ResendVerificationEmailStatus status;

    public ResendVerificationEmailCommandResponse(ResendVerificationEmailStatus status) {
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
