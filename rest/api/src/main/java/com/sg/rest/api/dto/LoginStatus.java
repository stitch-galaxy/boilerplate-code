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
public class LoginStatus {

    public enum Status {

        EMAIL_NOT_REGISTERED,
        EMAIL_NOT_VERIFIED,
        PASSWORD_INCORRECT,
        SUCCESS;
    }

    private final Status status;
    private final TokenInfo info;

    private LoginStatus(Status status,
                        TokenInfo info
    ) {
        this.status = status;
        this.info = info;
    }

    public static LoginStatus getErrorLoginStatus(Status status) {
        return new LoginStatus(status, null);
    }

    public static LoginStatus getSuccessLoginStatus(TokenInfo info) {
        return new LoginStatus(Status.SUCCESS, info);
    }

}
