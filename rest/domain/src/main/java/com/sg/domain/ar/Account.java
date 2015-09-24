/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.ar;

import com.sg.domain.vo.AccountId;
import com.sg.domain.vo.Email;
import com.sg.domain.vo.PasswordHash;

/**
 *
 * @author Admin
 */
public class Account {

    private final AccountId accountId;
    private EmailAccount emailAccount;
    private FacebookAccount facebookAccount;

    private Account(AccountId accountId,
            EmailAccount emailAccount,
            FacebookAccount facebookAccount) {
        this.accountId = accountId;
        this.emailAccount = emailAccount;
        this.facebookAccount = facebookAccount;

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
    
    public static Account registerEmailAccount(Email email, PasswordHash passwordHash)
    {
        AccountId accountId = AccountId.create();
        EmailAccount emailAccount = new EmailAccount(email, passwordHash, false);
        return new Account(accountId, emailAccount, null);
    }
}
