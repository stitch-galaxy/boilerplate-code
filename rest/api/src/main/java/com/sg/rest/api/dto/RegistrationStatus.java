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
public class RegistrationStatus {

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {

        PASSWORD_INVALID,
        EMAIL_INVALID,
        EMAIL_ALREADY_REGISTERED,
        SUCCESS;
    }

    private Status status;

    public RegistrationStatus(Status status) {
        this.status = status;
    }
}
