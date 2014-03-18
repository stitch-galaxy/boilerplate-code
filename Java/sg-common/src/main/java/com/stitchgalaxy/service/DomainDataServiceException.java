/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.service;

/**
 *
 * @author tarasev
 */
public class DomainDataServiceException extends Exception {
    
    public DomainDataServiceException(String message)
    {
        super(message);
    }
    
    public DomainDataServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public DomainDataServiceException(Throwable cause)
    {
        super(cause);
    }
}
