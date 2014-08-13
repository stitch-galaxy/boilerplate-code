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
    
    @Value( "${security.key}" )
    private String secret;
    
    public String getTokenString(AuthToken token) throws IOException{
        
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(secret);
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(token);
        return textEncryptor.encrypt(json);
    }
    
    public AuthToken getTokenFromString(String encryptedToken) throws IOException
    {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(secret);
        
        String tokenBody = textEncryptor.decrypt(encryptedToken);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(tokenBody, AuthToken.class);
    }
}
