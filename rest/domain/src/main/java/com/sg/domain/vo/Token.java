/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.vo;

/**
 *
 * @author tarasev
 */
public class Token {

    /**
     * @return the accountId
     */
    public AccountId getAccountId() {
        return accountId;
    }

    /**
     * @return the tokenType
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    private final AccountId accountId;
    private final TokenType tokenType;
    private final TokenIdentity tokenIdentity;

    public Token(AccountId accountId,
                 TokenType tokenType,
                 TokenIdentity tokenIdentity) {
        if (accountId == null || tokenType == null || tokenIdentity == null) {
            throw new IllegalArgumentException();
        }
        this.accountId = accountId;
        this.tokenType = tokenType;
        this.tokenIdentity = tokenIdentity;
    }

    /**
     * @return the tokenIdentity
     */
    public TokenIdentity getTokenIdentity() {
        return tokenIdentity;
    }

}
