/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.service.websecurity.components;

import com.sg.rest.authtoken.AuthTokenComponent;
import com.sg.rest.authtoken.BadTokenException;
import com.sg.rest.authtoken.Token;
import com.sg.rest.authtoken.TokenExpiredException;
import com.sg.rest.authtoken.jwt.JwtAuthTokenComponent;
import com.sg.rest.service.websecurity.WebSecurityBadTokenException;
import com.sg.rest.service.websecurity.WebSecurityTokenExpiredException;
import com.sg.rest.service.websecurity.WebSecurityService;
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
public class WebSecurityServiceImpl implements WebSecurityService {

    private final AuthTokenComponent authTokenComponent;

    @Autowired
    public WebSecurityServiceImpl(@Value("${security.key}") String symmetricKey) {
        this.authTokenComponent = new JwtAuthTokenComponent(symmetricKey, Duration.standardMinutes(2));
    }

    @Override
    public String generateToken(Long accountId, Instant issuedAt, Duration validDuration) {
        Token token = new Token();
        token.setUid(accountId.toString());

        return authTokenComponent.signToken(token, issuedAt, issuedAt.plus(validDuration));
    }

    @Override
    public Long getAccountIdAndVerifyToken(String sToken) throws WebSecurityBadTokenException, WebSecurityTokenExpiredException {
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
