/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.authtoken.jwt;

import com.sg.domain.exceptions.BadTokenException;
import com.sg.domain.exceptions.TokenExpiredException;
import java.util.UUID;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author tarasev
 */
public class JwtAuthTokenServiceTest {

    private static final String SECURITY_KEY = "kjdhasdkjhaskdjhaskdjhaskdjh";
    private static final UUID UID = UUID.randomUUID();
    private static final UUID TOKEN_IDENTITY_UID = UUID.randomUUID();
    private static final int TYPE = 1;
    private static final long ACCEPTABLE_CLOCK_SKEW_MINUTES = 2;

    private final JwtAuthTokenService jwtComponent = new JwtAuthTokenService(SECURITY_KEY, Duration.standardMinutes(ACCEPTABLE_CLOCK_SKEW_MINUTES));

    @Test
    public void testValidToken() throws BadTokenException, TokenExpiredException {
        JwtToken toSign = new JwtToken(UID, TYPE, TOKEN_IDENTITY_UID);
        Instant now = Instant.now();
        String signedToken = jwtComponent.signToken(toSign, now, now.plus(Duration.standardHours(1)));

        JwtToken parsed = jwtComponent.verifySignatureAndExtractToken(signedToken);
        Assert.assertEquals(toSign, parsed);
    }

    @Test
    public void testValidClocksShift() throws BadTokenException, TokenExpiredException {
        JwtToken toSign = new JwtToken(UID, TYPE, TOKEN_IDENTITY_UID);
        Instant now = Instant.now();
        Instant nowMinusAcceptableSkewPlusOneMinute = now.minus(Duration.standardMinutes(ACCEPTABLE_CLOCK_SKEW_MINUTES)).plus(Duration.standardMinutes(1));
        Instant oneHourAgo = now.minus(Duration.standardHours(1));

        String signedToken = jwtComponent.signToken(toSign, oneHourAgo, nowMinusAcceptableSkewPlusOneMinute);

        JwtToken parsed = jwtComponent.verifySignatureAndExtractToken(signedToken);
        Assert.assertEquals(toSign, parsed);
    }

    @Test
    public void testInvalidClocksShift() throws BadTokenException, TokenExpiredException {
        JwtToken toSign = new JwtToken(UID, TYPE, TOKEN_IDENTITY_UID);
        Instant now = Instant.now();
        Instant nowMinusAcceptableSkew = now.minus(Duration.standardMinutes(ACCEPTABLE_CLOCK_SKEW_MINUTES));
        Instant oneHourAgo = now.minus(Duration.standardHours(1));

        String signedToken = jwtComponent.signToken(toSign, oneHourAgo, nowMinusAcceptableSkew);
        try {
            JwtToken parsed = jwtComponent.verifySignatureAndExtractToken(signedToken);
            Assert.fail("Expected " + TokenExpiredException.class.getSimpleName());
        } catch (TokenExpiredException e) {
        }
    }

    @Test
    public void testExpiredToken() throws BadTokenException {
        JwtToken toSign = new JwtToken(UID, TYPE, TOKEN_IDENTITY_UID);
        Instant oneHourAgo = Instant.now().minus(Duration.standardHours(1));
        Instant thirtyMinutesAgo = oneHourAgo.plus(Duration.standardMinutes(30));
        String signedToken = jwtComponent.signToken(toSign, oneHourAgo, thirtyMinutesAgo);

        try {
            jwtComponent.verifySignatureAndExtractToken(signedToken);
            Assert.fail("Expected " + TokenExpiredException.class.getSimpleName());
        } catch (TokenExpiredException e) {
        }
    }

    @Test
    public void testInvalidToken() throws BadTokenException, TokenExpiredException {
        String invalidToken = "invalidToken";

        try {
            jwtComponent.verifySignatureAndExtractToken(invalidToken);
            Assert.fail("Expected " + BadTokenException.class.getSimpleName());
        } catch (BadTokenException e) {
        }
    }

    @Test
    public void testTokenWithInvalidSignature() throws BadTokenException, TokenExpiredException {
        JwtToken toSign = new JwtToken(UID, TYPE, TOKEN_IDENTITY_UID);
        Instant now = Instant.now();
        String signedToken = jwtComponent.signToken(toSign, now, now.plus(Duration.standardHours(1)));
        int idx = signedToken.lastIndexOf(".");
        String tokenWithBadSignature = signedToken.substring(0, idx);
        tokenWithBadSignature += ".badsignature";

        try {
            jwtComponent.verifySignatureAndExtractToken(tokenWithBadSignature);
            Assert.fail("Expected " + BadTokenException.class.getSimpleName());
        } catch (BadTokenException e) {
        }
    }

    @Test
    public void testTokenWithBadIatEat() throws BadTokenException, TokenExpiredException {
        JwtToken toSign = new JwtToken(UID, TYPE, TOKEN_IDENTITY_UID);
        Instant now = Instant.now();
        Instant oneHourAgo = now.minus(Duration.standardHours(1));
        String signedToken = jwtComponent.signToken(toSign, now, oneHourAgo);

        try {
            jwtComponent.verifySignatureAndExtractToken(signedToken);
            Assert.fail("Expected " + BadTokenException.class.getSimpleName());
        } catch (BadTokenException e) {
        }
    }

    @Test
    public void testInvlidJwtAuthTokenService() {
        try {
            JwtAuthTokenService jwtAuthTokenService = new JwtAuthTokenService(null, Duration.standardMinutes(ACCEPTABLE_CLOCK_SKEW_MINUTES));
            Assert.fail("Expected " + RuntimeException.class.getSimpleName());
        } catch (RuntimeException e) {
        }
    }

}
