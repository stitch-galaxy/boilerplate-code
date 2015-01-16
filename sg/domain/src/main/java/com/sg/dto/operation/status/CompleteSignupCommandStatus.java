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
public enum CompleteSignupCommandStatus {
    STATUS_ACCOUNT_NOT_FOUND,
    STATUS_ACCOUNT_NOT_LINKED_TO_EMAIL,
    STATUS_ALREADY_COMPLETED,
    STATUS_SUCCESS;
}
