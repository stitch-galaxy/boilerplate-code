/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.exception;

/**
 *
 * @author tarasev
 */
public class SgThreadAlreadyExistsException extends SgServiceLayerRuntimeException {
    public SgThreadAlreadyExistsException(String code)
    {
        super("Thread " + code + " already exists.");
    }
    
}
