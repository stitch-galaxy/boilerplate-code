/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.rest.service.securitytoken;

import com.sg.rest.authtoken.BadTokenException;
import com.sg.rest.authtoken.TokenExpiredException;
import org.joda.time.Duration;
import org.joda.time.Instant;

/**
 *
 * @author tarasev
 */
public interface WebSecurityTokenService {
    
    public String generateToken(Long accountId, Instant issuedAt, Duration validDuration);
    public Long getAccountIdAndVerifyToken(String sToken)  throws BadTokenException, TokenExpiredException;
    
}
