/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.services;

import com.sg.domain.exceptions.BadTokenException;
import com.sg.domain.exceptions.TokenExpiredException;
import com.sg.domain.vo.Token;
import com.sg.domain.vo.TokenSignature;

/**
 *
 * @author Admin
 */
public interface AuthTokenService {
    
    public TokenSignature signToken(Token token);
    public Token verifyToken(String signedToken) throws BadTokenException, TokenExpiredException;
}
