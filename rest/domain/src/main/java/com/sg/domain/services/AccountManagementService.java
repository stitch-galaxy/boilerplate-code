/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.services;

import com.sg.domain.ar.Account;
import com.sg.domain.events.AccountRegistrationEvent;
import com.sg.domain.events.ResendRegistrationConfirmationEmailEvent;
import com.sg.domain.events.SendResetPasswordLinkEvent;
import com.sg.domain.exceptions.EmailAlreadyVerifiedException;
import com.sg.domain.exceptions.EmailInvalidException;
import com.sg.domain.exceptions.EmailIsNotUniqueException;
import com.sg.domain.exceptions.EmailNotRegisteredException;
import com.sg.domain.exceptions.NonEmailAccountException;
import com.sg.domain.exceptions.PasswordDoNotMatchException;
import com.sg.domain.exceptions.PasswordInvalidException;
import com.sg.domain.repositories.AccountRepository;
import com.sg.domain.specs.EmailIsUniqueSpecification;
import com.sg.domain.specs.EmailIsValidSpecification;
import com.sg.domain.specs.PasswordIsValidSpecification;
import com.sg.domain.vo.Email;
import com.sg.domain.vo.PasswordHash;
import com.sg.domain.vo.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class AccountManagementService {
    
    private final EmailIsValidSpecification emailIsValidSpecification;
    private final PasswordIsValidSpecification passwordIsValidSpecification;
    private final EmailIsUniqueSpecification emailIsUniqueSpecification;
    private final PasswordHashService passwordHasher;
    private final AccountRepository accountRepository;
    private final DomainEventsRouter eventsRouter;
    
    @Autowired
    public AccountManagementService(
            EmailIsValidSpecification emailIsValidSpecification,
            PasswordIsValidSpecification passwordIsValidSpecification,
            EmailIsUniqueSpecification emailIsUniqueSpecification,
            PasswordHashService passwordHasher,
            AccountRepository accountRepository,
            DomainEventsRouter eventsRouter) {
        this.emailIsValidSpecification = emailIsValidSpecification;
        this.passwordIsValidSpecification = passwordIsValidSpecification;
        this.emailIsUniqueSpecification = emailIsUniqueSpecification;
        this.passwordHasher = passwordHasher;
        this.accountRepository = accountRepository;
        this.eventsRouter = eventsRouter;
    }
    
    public void registerEmailAccount(String sEmail,
                                     String password) throws EmailIsNotUniqueException, EmailInvalidException, PasswordInvalidException {
        Email email = new Email(sEmail);
        if (!emailIsValidSpecification.isSatisfiedBy(email)) {
            throw new EmailInvalidException();
        }
        if (!passwordIsValidSpecification.isSatisfiedBy(password)) {
            throw new PasswordInvalidException();
        }
        if (!emailIsUniqueSpecification.isSatisfiedBy(email)) {
            throw new EmailIsNotUniqueException();
        }
        PasswordHash hash = passwordHasher.getHash(password);
        Account account = Account.registerEmailAccount(email, hash);
        account.registerTokenType(TokenType.REGISTRATION_CONFIRMATION_TOKEN);
        accountRepository.create(account);
        AccountRegistrationEvent event = new AccountRegistrationEvent(account.getAccountId());
        eventsRouter.routeEvent(event);
    }
    
    public void resendRegistrationConfirmationEmail(String sEmail) throws EmailNotRegisteredException, EmailAlreadyVerifiedException {
        Email email = new Email(sEmail);
        Account account = accountRepository.getAccountByEmail(email);
        if (account == null) {
            throw new EmailNotRegisteredException();
        }
        if (account.getEmailAccount().isRegistrationConfirmed()) {
            throw new EmailAlreadyVerifiedException();
        }
        account.revokeToken(TokenType.REGISTRATION_CONFIRMATION_TOKEN);
        
        ResendRegistrationConfirmationEmailEvent event = new ResendRegistrationConfirmationEmailEvent(account.getAccountId());
        eventsRouter.routeEvent(event);
    }
    
    public void sendPasswordResetLink(String sEmail) throws EmailNotRegisteredException {
        Email email = new Email(sEmail);
        Account account = accountRepository.getAccountByEmail(email);
        if (account == null) {
            throw new EmailNotRegisteredException();
        }
        if (account.isTokenTypeRegistered(TokenType.PASWORD_RESET_TOKEN)) {
            account.revokeToken(TokenType.PASWORD_RESET_TOKEN);
        } else {
            account.registerTokenType(TokenType.PASWORD_RESET_TOKEN);
        }
        
        accountRepository.update(account);
        SendResetPasswordLinkEvent event = new SendResetPasswordLinkEvent(account.getAccountId());
        eventsRouter.routeEvent(event);
    }
    
    public void confirmRegistration(Account account) throws NonEmailAccountException, EmailAlreadyVerifiedException {
        if (account.getEmailAccount() == null) {
            throw new NonEmailAccountException();
        }
        if (account.getEmailAccount().isRegistrationConfirmed()) {
            throw new EmailAlreadyVerifiedException();
        }
        account.getEmailAccount().verify();
        //account.unregisterTokenType(TokenType.REGISTRATION_CONFIRMATION_TOKEN);
        account.registerTokenType(TokenType.WEB_SESSION_TOKEN);
        accountRepository.update(account);
    }
    
    public void resetPassword(Account account,
                              String password) throws NonEmailAccountException, PasswordInvalidException {
        if (account.getEmailAccount() == null) {
            throw new NonEmailAccountException();
        }
        if (!passwordIsValidSpecification.isSatisfiedBy(password)) {
            throw new PasswordInvalidException();
        }
        PasswordHash hash = passwordHasher.getHash(password);
        account.getEmailAccount().resetPassword(hash);
        account.unregisterTokenType(TokenType.PASWORD_RESET_TOKEN);
        accountRepository.update(account);
    }
    
    public void changePassword(Account account,
                               String oldPassword,
                               String newPassword) throws NonEmailAccountException, PasswordDoNotMatchException, PasswordInvalidException {
        
        if (account.getEmailAccount() == null) {
            throw new NonEmailAccountException();
        }
        PasswordHash oldPasswordHash = passwordHasher.getHash(oldPassword);
        if (!account.getEmailAccount().checkPassword(oldPasswordHash)) {
            throw new PasswordDoNotMatchException();
        }
        if (!passwordIsValidSpecification.isSatisfiedBy(newPassword)) {
            throw new PasswordInvalidException();
        }
        PasswordHash newPasswordHash = passwordHasher.getHash(newPassword);
        account.getEmailAccount().resetPassword(newPasswordHash);
        accountRepository.update(account);
    }
}
