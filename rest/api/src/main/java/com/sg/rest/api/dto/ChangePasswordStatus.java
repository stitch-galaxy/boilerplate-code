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
public class ChangePasswordStatus {

    public enum Status {

        NOT_EMAIL_ACCOUNT,
        OLD_PASSWORD_INCORRECT,
        NEW_PASSWORD_INVALID,
        SUCCESS;
    }

    private final Status status;

    public ChangePasswordStatus(Status status
    ) {
        this.status = status;
    }

}
