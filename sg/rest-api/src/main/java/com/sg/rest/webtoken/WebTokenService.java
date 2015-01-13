/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.rest.webtoken;

import org.joda.time.Duration;
import org.joda.time.Instant;

/**
 *
 * @author tarasev
 */
public interface WebTokenService {
    
    public String generateToken(long accountId, Instant issuedAt, Duration validDuration);
    public long getAccountIdAndVerifyToken(String sToken)  throws WebSecurityAccountNotFoundException, WebSecurityBadTokenException, WebSecurityTokenExpiredException;
    
}
