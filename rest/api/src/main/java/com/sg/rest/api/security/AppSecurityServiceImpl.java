/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api.security;

import com.sg.domain.ar.Account;
import com.sg.domain.exceptions.BadTokenException;
import com.sg.domain.exceptions.TokenExpiredException;
import com.sg.domain.repositories.AccountRepository;
import com.sg.domain.services.AuthTokenService;
import com.sg.domain.services.PasswordHasher;
import com.sg.domain.vo.Email;
import com.sg.domain.vo.PasswordHash;
import com.sg.domain.vo.Token;
import com.sg.domain.vo.TokenSignature;
import com.sg.domain.vo.TokenType;
import com.sg.rest.api.dto.LoginStatus;
import com.sg.rest.api.dto.TokenInfo;
import com.sg.rest.api.security.exceptions.AppSecurityAccountNotFoundException;
import com.sg.rest.api.security.exceptions.AppSecurityBadTokenException;
import com.sg.rest.api.security.exceptions.AppSecurityNoTokenException;
import com.sg.rest.api.security.exceptions.AppSecurityTokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class AppSecurityServiceImpl implements AppSecurityService {

    private final AuthTokenService authTokenService;
    private final AccountRepository accountRepository;
    private final PasswordHasher passwordHasher;

    @Autowired
    public AppSecurityServiceImpl(AuthTokenService authTokenService,
                                  AccountRepository accountRepository,
                                  PasswordHasher passwordHasher) {
        this.authTokenService = authTokenService;
        this.accountRepository = accountRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public Account getTokenAccount(String sToken) {
        try {
            if (sToken == null || sToken.isEmpty()) {
                throw new AppSecurityNoTokenException();
            }
            Token token = authTokenService.verifyToken(sToken);

            Account account = accountRepository.getAccountByAccountId(token.getAccountId());
            if (account == null) {
                throw new AppSecurityAccountNotFoundException();
            }
            return account;
        } catch (BadTokenException e) {
            throw new AppSecurityBadTokenException(e);
        } catch (TokenExpiredException e) {
            throw new AppSecurityTokenExpiredException(e);
        }
    }

    @Override
    public LoginStatus login(String email,
                             String password) {
        Account account = accountRepository.getAccountByEmail(new Email(email));
        if (account == null) {
            return LoginStatus.getErrorLoginStatus(LoginStatus.Status.EMAIL_NOT_REGISTERED);
        }
        if (!account.getEmailAccount().isVerified()) {
            return LoginStatus.getErrorLoginStatus(LoginStatus.Status.EMAIL_NOT_VERIFIED);
        }
        PasswordHash hash = passwordHasher.getHash(password);
        if (!account.getEmailAccount().checkPassword(hash)) {
            return LoginStatus.getErrorLoginStatus(LoginStatus.Status.PASSWORD_INCORRECT);
        }
        TokenSignature signature = authTokenService.signToken(new Token(account.getAccountId(), TokenType.WEB_SESSION_TOKEN));
        return LoginStatus.getSuccessLoginStatus(new TokenInfo(signature.getToken(), signature.getExpiresAt().toEpochMilli()));
    }

}
