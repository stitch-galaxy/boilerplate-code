/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.command;

import com.sg.mail.service.EmailService;
import com.sg.rest.authtoken.Token;

/**
 *
 * @author tarasev
 */
public class TokenEmailPublisher {

    private final TokenComponent securityComponent;
    private final EmailService emailService;

    public TokenEmailPublisher(TokenComponent securityComponent, EmailService emailService) {
        this.securityComponent = securityComponent;
        this.emailService = emailService;
    }

    public void sendVerificationEmail(long accountId, String email) {
        Token token = new Token();
        token.setUid(String.valueOf(accountId));

        String tokenString = securityComponent.generateEmailToken(accountId);
        emailService.sendVerificationEmail(tokenString, email);
    }
}
