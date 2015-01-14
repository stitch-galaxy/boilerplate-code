/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.command;

import com.sg.rest.authtoken.AuthTokenComponent;
import com.sg.rest.authtoken.Token;
import com.sg.rest.authtoken.enumerations.TokenExpirationStandardDurations;
import org.joda.time.Instant;

/**
 *
 * @author tarasev
 */
public class TokenComponent {
    private final AuthTokenComponent securityComponent;
    
    public TokenComponent(AuthTokenComponent securityComponent) {
        this.securityComponent = securityComponent;
    }

    
    public String generateSessionToken(long accountId)
    {
        Token token = new Token();
        token.setUid(String.valueOf(accountId));

        return securityComponent.signToken(token, Instant.now(), TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION);
    }
    
    public String generateEmailToken(long accountId)
    {
        Token token = new Token();
        token.setUid(String.valueOf(accountId));

        return securityComponent.signToken(token, Instant.now(), TokenExpirationStandardDurations.EMAIL_TOKEN_EXPIRATION_DURATION);
    }
    
}
