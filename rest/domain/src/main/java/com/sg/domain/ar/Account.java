/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.ar;

import com.sg.domain.vo.AccountId;
import com.sg.domain.vo.Email;
import com.sg.domain.vo.PasswordHash;
import com.sg.domain.vo.TokenIdentity;
import com.sg.domain.vo.TokenType;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class Account {

    private final AccountId accountId;
    private EmailAccount emailAccount;
    private FacebookAccount facebookAccount;
    private final Map<TokenType, TokenIdentity> tokenIdentites = new EnumMap<>(TokenType.class);

    private Account(AccountId accountId,
                    EmailAccount emailAccount,
                    FacebookAccount facebookAccount,
                    Map<TokenType, TokenIdentity> tokenIdentites) {
        this.accountId = accountId;
        this.emailAccount = emailAccount;
        this.facebookAccount = facebookAccount;
        if (tokenIdentites != null) {
            this.tokenIdentites.putAll(tokenIdentites);
        }

    }

    /**
     * @return the emailAccount
     */
    public EmailAccount getEmailAccount() {
        return emailAccount;
    }

    /**
     * @return the accountId
     */
    public AccountId getAccountId() {
        return accountId;
    }

    public static Account registerEmailAccount(Email email,
                                               PasswordHash passwordHash) {
        AccountId accountId = AccountId.create();
        EmailAccount emailAccount = new EmailAccount(email, passwordHash, false);
        return new Account(accountId, emailAccount, null, null);
    }

    public TokenIdentity getTokenIdentity(TokenType tokenType) {
        TokenIdentity identity = tokenIdentites.get(tokenType);
        if (identity == null) {
            identity = TokenIdentity.create();
            tokenIdentites.put(tokenType, identity);
        }
        return identity;
    }

    public void revokeToken(TokenType tokenType) {
        TokenIdentity identity = TokenIdentity.create();
        tokenIdentites.put(tokenType, identity);
    }

    public void revokeAllTokens() {
        for (TokenType tokenType : tokenIdentites.keySet()) {
            tokenIdentites.put(tokenType, TokenIdentity.create());
        }
    }
}
