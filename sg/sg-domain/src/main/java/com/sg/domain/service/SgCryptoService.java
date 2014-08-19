/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import com.sg.domain.service.exception.SgCryptoException;

/**
 *
 * @author tarasev
 */
public interface SgCryptoService {
    
    public String getTokenString(AuthToken token) throws SgCryptoException;
    public AuthToken getTokenFromString(String encryptedToken) throws SgCryptoException;
    
}
