/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.services;

import com.sg.domain.ar.Account;
import com.sg.domain.exceptions.BadTokenException;
import com.sg.domain.exceptions.EmailNotRegisteredException;
import com.sg.domain.exceptions.EmailNotVerifiedException;
import com.sg.domain.exceptions.PasswordDoNotMatchException;
import com.sg.domain.exceptions.TokenBasedSecurityAccountNotFoundException;
import com.sg.domain.exceptions.TokenBasedSecurityBadTokenException;
import com.sg.domain.exceptions.TokenBasedSecurityNoTokenException;
import com.sg.domain.exceptions.TokenBasedSecurityNotAcceptableTokenTypeException;
import com.sg.domain.exceptions.TokenBasedSecurityTokenExpiredException;
import com.sg.domain.exceptions.TokenBasedSecurityTokenRevokedException;
import com.sg.domain.exceptions.TokenBasedSecurityTokenTypeNotAvailiableException;
import com.sg.domain.exceptions.TokenExpiredException;
import com.sg.domain.repositories.AccountRepository;
import com.sg.domain.vo.Email;
import com.sg.domain.vo.PasswordHash;
import com.sg.domain.vo.Token;
import com.sg.domain.vo.TokenIdentity;
import com.sg.domain.vo.TokenSignature;
import com.sg.domain.vo.TokenType;
import java.util.EnumSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class TokenBasedSecurityService {

    private final AuthTokenService authTokenService;
    private final AccountRepository accountRepository;
    private final PasswordHashService passwordHasher;
    
    public final static Set<TokenType> APP_TOKEN = EnumSet.of(TokenType.WEB_SESSION_TOKEN);
    public static final Set<TokenType> REGISTRATION_CONFIRMATION_TOKEN = EnumSet.of(TokenType.REGISTRATION_CONFIRMATION_TOKEN);
    public final static Set<TokenType> PASWORD_RESET_TOKEN = EnumSet.of(TokenType.PASWORD_RESET_TOKEN);

    @Autowired
    public TokenBasedSecurityService(AuthTokenService authTokenService,
                                     AccountRepository accountRepository,
                                     PasswordHashService passwordHasher) {
        this.authTokenService = authTokenService;
        this.accountRepository = accountRepository;
        this.passwordHasher = passwordHasher;
    }

    private Token parseToken(String sToken) {
        try {
            if (sToken == null || sToken.isEmpty()) {
                throw new TokenBasedSecurityNoTokenException();
            }
            return authTokenService.verifyToken(sToken);
        } catch (BadTokenException e) {
            throw new TokenBasedSecurityBadTokenException(e);
        } catch (TokenExpiredException e) {
            throw new TokenBasedSecurityTokenExpiredException(e);
        }
    }

    public Account getAccount(String sToken, Set<TokenType> acceptableTokenTypes) {
        Token token = parseToken(sToken);
        if (!acceptableTokenTypes.contains(token.getTokenType()))
            throw new TokenBasedSecurityNotAcceptableTokenTypeException();
        Account account = accountRepository.getAccountByAccountId(token.getAccountId());
        if (account == null) {
            throw new TokenBasedSecurityAccountNotFoundException();
        }
        TokenIdentity tokenIdentity = account.getTokenIdentity(token.getTokenType());
        if (tokenIdentity == null)
        {
            throw new TokenBasedSecurityTokenTypeNotAvailiableException();
        }
        if (!tokenIdentity.equals(token.getTokenIdentity()))
            throw new TokenBasedSecurityTokenRevokedException();
        return account;
    }

    public TokenSignature login(String email,
                                String password) throws EmailNotRegisteredException, EmailNotVerifiedException, PasswordDoNotMatchException {
        Account account = accountRepository.getAccountByEmail(new Email(email));
        if (account == null) {
            throw new EmailNotRegisteredException();
        }
        if (!account.getEmailAccount().isRegistrationConfirmed()) {
            throw new EmailNotVerifiedException();
        }
        PasswordHash hash = passwordHasher.getHash(password);
        if (!account.getEmailAccount().checkPassword(hash)) {
            throw new PasswordDoNotMatchException();
        }
        return authTokenService.signToken(new Token(account.getAccountId(), TokenType.WEB_SESSION_TOKEN, account.getTokenIdentity(TokenType.WEB_SESSION_TOKEN)));
    }
}
