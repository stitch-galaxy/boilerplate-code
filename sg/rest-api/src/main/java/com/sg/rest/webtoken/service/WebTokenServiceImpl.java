/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.webtoken.service;

import com.sg.rest.authtoken.enumerations.TokenExpirationStandardDurations;
import com.sg.rest.authtoken.AuthTokenComponent;
import com.sg.rest.authtoken.BadTokenException;
import com.sg.rest.authtoken.Token;
import com.sg.rest.authtoken.TokenExpiredException;
import com.sg.rest.authtoken.jwt.JwtAuthTokenComponent;
import com.sg.rest.webtoken.WebSecurityBadTokenException;
import com.sg.rest.webtoken.WebSecurityTokenExpiredException;
import com.sg.rest.webtoken.WebTokenService;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author tarasev
 */
@Service
public class WebTokenServiceImpl implements WebTokenService {

    private final AuthTokenComponent authTokenComponent;

    @Autowired
    public WebTokenServiceImpl(@Value("${com.sg.security.key}") String symmetricKey) {
        this.authTokenComponent = new JwtAuthTokenComponent(symmetricKey, Duration.standardMinutes(2));
    }

    @Override
    public String generateToken(long accountId, Instant issuedAt, TokenExpirationStandardDurations validDuration) {
        Token token = new Token();
        token.setUid(String.valueOf(accountId));

        return authTokenComponent.signToken(token, issuedAt, validDuration);
    }

    @Override
    public long getAccountIdAndVerifyToken(String sToken) throws WebSecurityBadTokenException, WebSecurityTokenExpiredException {
        try {
            Token token = authTokenComponent.verifySignatureAndExtractToken(sToken);
            return Long.parseLong(token.getUid());
        } catch (BadTokenException e) {
            throw new WebSecurityBadTokenException(e);
        } catch (TokenExpiredException e) {
            throw new WebSecurityTokenExpiredException(e);
        } catch (NumberFormatException e) {
            throw new WebSecurityBadTokenException(e);
        }
    }

}
