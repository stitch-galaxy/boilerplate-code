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
public class SignupStatus {

    public enum Status {

        PASSWORD_INVALID,
        EMAIL_INVALID,
        EMAIL_ALREADY_REGISTERED,
        SUCCESS;
    }

    private final Status status;

    private SignupStatus(Status status) {
        this.status = status;
    }

    public static SignupStatus fromStatus(Status status) {
        return new SignupStatus(status);
    }

}
