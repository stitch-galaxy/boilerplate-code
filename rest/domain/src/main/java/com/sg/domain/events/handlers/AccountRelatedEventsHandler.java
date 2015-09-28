/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.events.handlers;

import com.sg.domain.ar.Account;
import com.sg.domain.events.AccountRegistrationEvent;
import com.sg.domain.events.ResendRegistrationConfirmationEmailEvent;
import com.sg.domain.events.SendResetPasswordLinkEvent;
import com.sg.domain.repositories.AccountRepository;
import com.sg.domain.services.AuthTokenService;
import com.sg.domain.services.MailService;
import com.sg.domain.vo.AccountId;
import com.sg.domain.vo.Token;
import com.sg.domain.vo.TokenSignature;
import com.sg.domain.vo.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class AccountRelatedEventsHandler {
    
    private final AccountRepository accountRepository;
    private final MailService mailService;
    private final AuthTokenService authTokenService;
    
    @Autowired
    public AccountRelatedEventsHandler(AccountRepository accountRepository,
                                       MailService mailService,
                                       AuthTokenService authTokenService) {
        this.accountRepository = accountRepository;
        this.mailService = mailService;
        this.authTokenService = authTokenService;
    }
    
    private void sendVerificationEmail(AccountId accountId) {
        Account account = accountRepository.getAccountByAccountId(accountId);
        if (account != null) {
            if (account.getEmailAccount() != null) {
                TokenSignature signature = authTokenService.signToken(new Token(
                        accountId,
                        TokenType.REGISTRATION_CONFIRMATION_TOKEN,
                        account.getTokenIdentity(TokenType.REGISTRATION_CONFIRMATION_TOKEN
                        )
                ));
                mailService.sendRegistrationConfirmationEmail(account.getEmailAccount().getEmail(), signature);
            }
        }
    }
    
    public void processEvent(AccountRegistrationEvent event) {
        sendVerificationEmail(event.getAccountId());
    }
    
    public void processEvent(ResendRegistrationConfirmationEmailEvent event) {
        sendVerificationEmail(event.getAccountId());
    }
    
    public void processEvent(SendResetPasswordLinkEvent event) {
        Account account = accountRepository.getAccountByAccountId(event.getAccountId());
        if (account != null) {
            if (account.getEmailAccount() != null) {
                TokenSignature signature = authTokenService.signToken(new Token(
                        event.getAccountId(),
                        TokenType.PASWORD_RESET_TOKEN,
                        account.getTokenIdentity(TokenType.PASWORD_RESET_TOKEN)
                ));
                mailService.sendResetPasswordLink(account.getEmailAccount().getEmail(), signature);
            }
        }
    }
    
}
