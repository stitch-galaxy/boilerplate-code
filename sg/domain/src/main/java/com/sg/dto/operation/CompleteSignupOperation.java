/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.operation;

/**
 *
 * @author tarasev
 */
public class CompleteSignupOperation implements Operation {

    private final long accountId;
    private final CompleteSignupParameters userCommand;

    /**
     * @return the accountId
     */
    public long getAccountId() {
        return accountId;
    }

    public CompleteSignupOperation(long accountId, CompleteSignupParameters userCommand) {
        if (userCommand == null)
            throw new IllegalArgumentException();
        this.accountId = accountId;
        this.userCommand = userCommand;
    }

    /**
     * @return the userCommand
     */
    public CompleteSignupParameters getUserCommand() {
        return userCommand;
    }
}
