/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.infrastructure;

import com.sg.domain.services.AuthTokenService;
import com.sg.domain.exceptions.BadTokenException;
import com.sg.domain.vo.Token;
import com.sg.domain.exceptions.TokenExpiredException;
import com.sg.domain.vo.TokenType;
import com.sg.domain.authtoken.jwt.JwtAuthTokenService;
import com.sg.domain.authtoken.jwt.JwtToken;
import com.sg.domain.vo.AccountId;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class AuthTokenServiceImpl implements AuthTokenService {
    
    private final JwtAuthTokenService jwtAuthTokenService;
    
    public AuthTokenServiceImpl(
            @Value("${com.sg.security.key}") String symmetricKey) {
        this.jwtAuthTokenService = new JwtAuthTokenService(symmetricKey, Duration.standardMinutes(1l));
    }
    
    @Override
    public String signToken(Token token) {
        JwtToken jwtToken = new JwtToken(token.getAccountId().getId(), token.getTokenType().getId());
        Instant now = Instant.now();
        Instant expiresAt = now.plus(getDurationForTokenType(token.getTokenType()));
        return jwtAuthTokenService.signToken(jwtToken, now, expiresAt);
    }
    
    @Override
    public Token verifyToken(String signedToken) throws BadTokenException, TokenExpiredException {
        JwtToken jwtToken = jwtAuthTokenService.verifySignatureAndExtractToken(signedToken);
        return new Token(new AccountId(jwtToken.getAccountId()), TokenType.parse(jwtToken.getTokenType()));
    }
    
    private Duration getDurationForTokenType(TokenType type) {
        switch (type) {
            case WEB_SESSION_TOKEN:
                return Duration.standardHours(1l);
            case EMAIL_VERIFICATION_TOKEN:
                return Duration.standardDays(60l);
            case PASWORD_RESET_TOKEN:
                return Duration.standardDays(1l);
        }
        return null;
    }
}
