/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.ar;

import com.sg.domain.vo.AccountId;
import com.sg.domain.vo.EmailAccountId;
import com.sg.domain.vo.FacebookAccountId;

/**
 *
 * @author Admin
 */
public class Account {

    private final AccountId accountId;
    private EmailAccountId emailAccountId;
    private FacebookAccountId facebookAccountId;

    Account(AccountId accountId,
            EmailAccountId emailAccountId,
            FacebookAccountId facebookAccountId) {
        this.accountId = accountId;
        this.emailAccountId = emailAccountId;
        this.facebookAccountId = facebookAccountId;

    }
}
