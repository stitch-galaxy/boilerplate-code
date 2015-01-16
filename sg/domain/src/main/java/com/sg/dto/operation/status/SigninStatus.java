/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.operation.status;

/**
 *
 * @author tarasev
 */
public enum SigninStatus {
    STATUS_ACCOUNT_NOT_FOUND,
    STATUS_ACCOUNT_NOT_LINKED_TO_EMAIL,
    STATUS_WRONG_PASSWORD,
    STATUS_EMAIL_NOT_VERIFIED,
    STATUS_SUCCESS;
}
