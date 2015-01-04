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
public class SgCanvasNotFoundException extends SgServiceLayerRuntimeException {
    public SgCanvasNotFoundException(String code)
    {
        super("Canvas " + code + " not found.");
    }
    
}
