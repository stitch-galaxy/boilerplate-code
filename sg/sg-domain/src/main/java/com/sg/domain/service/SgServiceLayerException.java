/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

/**
 *
 * @author tarasev
 */
public class SgServiceLayerException extends RuntimeException {

    public SgServiceLayerException(String message) {
        super(message);
    }
    
    public SgServiceLayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
