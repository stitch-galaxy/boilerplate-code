/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.authtoken;

/**
 *
 * @author tarasev
 */
import java.security.InvalidKeyException;
import java.security.SignatureException;
import net.oauth.jsontoken.Clock;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import net.oauth.jsontoken.SystemClock;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import net.oauth.jsontoken.crypto.HmacSHA256Verifier;
import net.oauth.jsontoken.crypto.SignatureAlgorithm;
import net.oauth.jsontoken.crypto.Verifier;
import net.oauth.jsontoken.discovery.VerifierProvider;
import net.oauth.jsontoken.discovery.VerifierProviders;
import net.oauth.signatures.SignedTokenAudienceChecker;
import org.joda.time.Duration;
import org.joda.time.Instant;

public class JwtAuthTokenService implements AuthTokenService {

    private final byte[] symmetricKey;
    private final Clock clock;
    private final JsonTokenParser tokenParser;
    private final HmacSHA256Signer signer;
    private final Verifier verifier;
    private final VerifierProviders verifierProviders;

    private static final String DOMAIN_URI = "http://sg.com";
    private static final String UID_PARAM = "uid";
    private static final String KEY_NAME = "symmetric_key";

    public JwtAuthTokenService(String sSymmetricKey, Duration acceptableClockSkew) {
        symmetricKey = sSymmetricKey.getBytes();
        clock = new SystemClock(acceptableClockSkew);

        try {
            signer = new HmacSHA256Signer(DOMAIN_URI, KEY_NAME, symmetricKey);
            verifier = new HmacSHA256Verifier(symmetricKey);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        VerifierProvider hmacLocator = new VerifierProviderContainer(verifier);
        verifierProviders = new VerifierProviders();
        verifierProviders.setVerifierProvider(SignatureAlgorithm.HS256, hmacLocator);

        tokenParser = new JsonTokenParser(verifierProviders, new SignedTokenAudienceChecker(DOMAIN_URI));
    }

    @Override
    public String signToken(Token token, Instant issuedAt, Instant expireAt) {
        JsonToken jsonWebToken = new JsonToken(signer, clock);
        jsonWebToken.setParam(UID_PARAM, token.getUid());
        jsonWebToken.setIssuedAt(issuedAt);
        jsonWebToken.setExpiration(expireAt);
        jsonWebToken.setAudience(DOMAIN_URI);

        try {
            return jsonWebToken.serializeAndSign();
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Token verifySignatureAndExtractToken(String signedToken) throws BadTokenException, TokenExpiredException {
        JsonToken jsonWebToken;
        try {
            jsonWebToken = tokenParser.deserialize(signedToken);
        } catch (Throwable t) {
            throw new BadTokenException(t);
        }
        try {
            tokenParser.verify(jsonWebToken);

            Token token = new Token();
            token.setUid(jsonWebToken.getParamAsPrimitive(UID_PARAM).getAsString());
            return token;
        } catch (SignatureException e) {
            throw new BadTokenException(e);
        } catch (IllegalStateException e) {
            Instant issuedAt = jsonWebToken.getIssuedAt();
            Instant expiration = jsonWebToken.getExpiration();

            if (issuedAt == null && expiration != null) {
                issuedAt = new Instant(0);
            }

            if (issuedAt != null && expiration == null) {
                expiration = new Instant(Long.MAX_VALUE);
            }
            if (issuedAt != null && expiration != null) {
                if (issuedAt.isAfter(expiration)) {
                    throw new BadTokenException(e);
                }
            }
            throw new TokenExpiredException(e);
        }

    }
}
