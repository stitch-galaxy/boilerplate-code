/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.events.handlers;

import com.sg.domain.ar.Account;
import com.sg.domain.events.AccountRegisteredEvent;
import com.sg.domain.events.ResendVerificationEmailEvent;
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
public class AccountRegistrationEventsHandler {

    private final AccountRepository accountRepository;
    private final MailService mailService;
    private final AuthTokenService authTokenService;

    @Autowired
    public AccountRegistrationEventsHandler(AccountRepository accountRepository,
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
                TokenSignature signature = authTokenService.signToken(new Token(accountId, TokenType.EMAIL_VERIFICATION_TOKEN));
                mailService.sendEmailVerificationMessage(account.getEmailAccount().getEmail(), signature);
            }
        }
    }

    public void processEvent(AccountRegisteredEvent event) {
        sendVerificationEmail(event.getAccountId());
    }

    public void processEvent(ResendVerificationEmailEvent event) {
        sendVerificationEmail(event.getAccountId());
    }

}
