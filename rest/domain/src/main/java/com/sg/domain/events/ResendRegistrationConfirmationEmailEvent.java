/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.events;

import com.sg.domain.vo.AccountId;

/**
 *
 * @author Admin
 */
public class ResendRegistrationConfirmationEmailEvent implements DomainEvent {

    private final AccountId accountId;

    public ResendRegistrationConfirmationEmailEvent(AccountId accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the accountId
     */
    public AccountId getAccountId() {
        return accountId;
    }
}
