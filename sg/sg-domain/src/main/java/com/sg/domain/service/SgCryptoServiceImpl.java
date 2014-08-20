/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.domain.service.exception.SgCryptoException;
import com.sg.domain.service.exception.SgInvalidTokenException;
import com.sg.domain.service.exception.SgTokenExpiredException;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jasypt.util.text.BasicTextEncryptor;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author tarasev
 */
@Service
public class SgCryptoServiceImpl implements SgCryptoService {

    @Value("${security.key}")
    private String secret;

    public String encryptSecurityToken(AuthToken token) throws SgCryptoException {

        try {
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword(secret);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(token);
            return textEncryptor.encrypt(json);
        } catch (Exception e) {
            throw new SgCryptoException(e);
        }
    }

    public AuthToken decryptSecurityTokenAtInstant(String encryptedToken, Instant instant) throws SgCryptoException {
        String tokenBody;
        AuthToken token;
        try {
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword(secret);

            tokenBody = textEncryptor.decrypt(encryptedToken);

        } catch (Exception e) {
            throw new SgInvalidTokenException(e);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            token = mapper.readValue(tokenBody, AuthToken.class);

        } catch (Exception e) {
            throw new SgInvalidTokenException(e);
        }
        if (token.isExpired(instant)) {
            throw new SgTokenExpiredException();
        }
        return token;

    }
}
