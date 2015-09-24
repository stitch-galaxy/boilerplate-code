/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api.security;

import com.sg.domain.ar.Account;
import com.sg.domain.vo.AccountId;
import com.sg.domain.vo.TokenType;

/**
 *
 * @author Admin
 */
public interface AppSecurityService {

    public Account getTokenAccount(String sToken);

    public String generateWebToken(AccountId accountId,
                                   TokenType tokenType);
}
