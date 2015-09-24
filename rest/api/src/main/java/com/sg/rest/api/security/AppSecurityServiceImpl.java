/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api.security;

import com.sg.rest.api.security.exceptions.AppSecurityAccountNotFoundException;
import com.sg.rest.api.security.exceptions.AppSecurityBadTokenException;
import com.sg.rest.api.security.exceptions.AppSecurityNoTokenException;
import com.sg.rest.api.security.exceptions.AppSecurityTokenExpiredException;
import com.sg.domain.authtoken.AuthTokenService;
import com.sg.domain.authtoken.BadTokenException;
import com.sg.domain.authtoken.Token;
import com.sg.domain.authtoken.TokenExpiredException;
import com.sg.domain.authtoken.TokenExpirationStandardDurations;
import com.sg.domain.authtoken.jwt.JwtAuthTokenComponent;
import java.util.Date;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class AppSecurityServiceImpl implements AppSecurityService {

    private static final String ACCOUNT_NOT_FOUND_MESSAGE_TEMPLATE = "Account %s not found";
    
    private final AuthTokenService authTokenComponent;

    @Autowired
    public AppSecurityServiceImpl(@Value("${com.sg.security.key}") String symmetricKey) {
        this.authTokenComponent = new JwtAuthTokenComponent(symmetricKey);
    }

    @Override
    public void verifyToken(String sToken) {
        try {
            if (sToken == null || sToken.isEmpty()) {
                throw new AppSecurityNoTokenException();
            }
            Token token = authTokenComponent.verifySignatureAndExtractToken(sToken);
            String uid = token.getUid();
            Date account = null;
            if (account == null)
            {
                throw new AppSecurityAccountNotFoundException(String.format(ACCOUNT_NOT_FOUND_MESSAGE_TEMPLATE, uid));
            }
        } catch (BadTokenException e) {
            throw new AppSecurityBadTokenException(e);
        } catch (TokenExpiredException e) {
            throw new AppSecurityTokenExpiredException(e);
        }
    }
    

    @Override
    public String generateWebToken(String uid) {
        Token token = new Token();
        token.setUid(uid);
        return authTokenComponent.signToken(token, Instant.now(), TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION);

    }

}
