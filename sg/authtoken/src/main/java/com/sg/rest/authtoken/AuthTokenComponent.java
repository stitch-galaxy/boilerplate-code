/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.authtoken;

import com.sg.rest.authtoken.enumerations.TokenExpirationStandardDurations;
import org.joda.time.Instant;

/**
 *
 * @author tarasev
 */
public interface AuthTokenComponent {

    public String signToken(Token token, Instant issuedAt, TokenExpirationStandardDurations validDuration);
    public String signToken(Token token, Instant issuedAt, Instant expireAt);
    public Token verifySignatureAndExtractToken(String signedToken) throws BadTokenException, TokenExpiredException;

}
