/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service.exception;

/**
 *
 * @author tarasev
 */
public class SgAccountWithoutEmailException extends SgServiceLayerRuntimeException {
    public SgAccountWithoutEmailException(Long accountId)
    {
        super("Account " + accountId + " do not have linked email.");
    }
    
}
