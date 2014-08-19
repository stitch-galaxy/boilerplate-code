/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.domain.service.exception.SgCryptoException;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jasypt.util.text.BasicTextEncryptor;
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

    public String getTokenString(AuthToken token) throws SgCryptoException {

        try {
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword(secret);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(token);
            return textEncryptor.encrypt(json);
        } catch (IOException e) {
            throw new SgCryptoException(e);
        }
    }

    public AuthToken getTokenFromString(String encryptedToken) throws SgCryptoException {
        String tokenBody;
        AuthToken token;
        try {
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword(secret);

            tokenBody = textEncryptor.decrypt(encryptedToken);

        } catch (Exception e) {
            throw new SgCryptoException("Can not decrypt security token", e);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            token = mapper.readValue(tokenBody, AuthToken.class);

        } catch (IOException e) {
            throw new SgCryptoException("Can not deserialize decrypted security exception", e);
        }
        if (token.isExpired()) {
            throw new SgCryptoException("Security token expired");
        }
        return token;

    }
}
