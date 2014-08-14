/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.security;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author tarasev
 */
@Component
public class Security {

    @Value("${security.key}")
    private String secret;

    public String getTokenString(AuthToken token) throws IOException {

        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(secret);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(token);
        return textEncryptor.encrypt(json);
    }

    public AuthToken getTokenFromString(String encryptedToken) throws SgSecurityException {
        String tokenBody;
        AuthToken token;
        try {
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword(secret);

            tokenBody = textEncryptor.decrypt(encryptedToken);

        } catch (Exception e) {
            throw new SgSecurityException("Can not decrypt security token", e);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            token = mapper.readValue(tokenBody, AuthToken.class);

        } catch (Exception e) {
            throw new SgSecurityException("Can not deserialize decoded security exception", e);
        }
        if (token.isExpired()) {
            throw new SgSecurityException("Security token expired");
        }
        return token;

    }
}
