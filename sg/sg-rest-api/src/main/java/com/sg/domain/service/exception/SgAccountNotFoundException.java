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
public class SgAccountNotFoundException extends SgServiceLayerRuntimeException {
    
    public SgAccountNotFoundException(String email)
    {
        super("Account for " + email + " registration email not found.");
    }
    
    public SgAccountNotFoundException(long accountId)
    {
        super("Account " + accountId + " not found.");
    }
    
}
