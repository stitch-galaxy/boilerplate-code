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
import com.sg.domain.vo.AccountId;
import com.sg.domain.vo.Token;
import com.sg.domain.vo.TokenType;
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

    @Autowired
    public AppSecurityServiceImpl(AuthTokenService authTokenService,
                                  AccountRepository accountRepository) {
        this.authTokenService = authTokenService;
        this.accountRepository = accountRepository;
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
    public String generateWebToken(AccountId accountId,
                                   TokenType tokenType) {
        Token token = new Token(accountId, tokenType);
        return authTokenService.signToken(token);
    }

}
