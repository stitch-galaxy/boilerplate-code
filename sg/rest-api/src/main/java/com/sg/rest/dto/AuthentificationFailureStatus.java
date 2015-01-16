/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.dto;

/**
 *
 * @author tarasev
 */
public enum AuthentificationFailureStatus {
    TOKEN_AUTHENTICATION_ACCOUNT_DO_NOT_EXISTS,
    TOKEN_AUTHENTICATION_BAD_TOKEN,
    TOKEN_AUTHENTICATION_TOKEN_EXPIRED,
    TOKEN_AUTHENTICATION_NO_TOKEN,
    UNKNOWN;
}
