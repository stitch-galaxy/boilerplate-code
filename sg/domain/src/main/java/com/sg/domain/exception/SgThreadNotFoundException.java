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
public class SgThreadNotFoundException extends SgServiceLayerRuntimeException {
    public SgThreadNotFoundException(String code)
    {
        super("Thread " + code + " not found.");
    }
    
}
