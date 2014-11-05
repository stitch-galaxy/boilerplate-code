/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.authtoken;

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
    private static final String UID = "tarasov.e.a@gmail.com";

    private final AuthTokenService jwtComponent = new JwtAuthTokenService(SECURITY_KEY);

    @Test
    public void testValidToken() throws BadTokenException, TokenExpiredException {
        Token toSign = new Token();
        toSign.setUid(UID);
        Instant now = Instant.now();
        String signedToken = jwtComponent.signToken(toSign, now, now.plus(Duration.standardHours(1)));

        Token parsed = jwtComponent.verifySignatureAndExtractToken(signedToken);
        Assert.assertEquals(UID, parsed.getUid());
    }

    @Test
    public void testExpiredToken() throws BadTokenException {
        Token toSign = new Token();
        toSign.setUid(UID);
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
        Token toSign = new Token();
        toSign.setUid(UID);
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
        Token toSign = new Token();
        toSign.setUid(UID);
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
            JwtAuthTokenService jwtAuthTokenService = new JwtAuthTokenService(null);
            Assert.fail("Expected " + RuntimeException.class.getSimpleName());
        } catch (RuntimeException e) {
        }
    }

}
