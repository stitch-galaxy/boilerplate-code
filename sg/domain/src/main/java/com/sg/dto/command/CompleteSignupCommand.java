/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.command;

/**
 *
 * @author tarasev
 */
public class CompleteSignupCommand implements Command {

    private final long accountId;
    private final CompleteSignupUserCommand userCommand;

    /**
     * @return the accountId
     */
    public long getAccountId() {
        return accountId;
    }

    public CompleteSignupCommand(long accountId, CompleteSignupUserCommand userCommand) {
        if (userCommand == null)
            throw new IllegalArgumentException();
        this.accountId = accountId;
        this.userCommand = userCommand;
    }

    /**
     * @return the userCommand
     */
    public CompleteSignupUserCommand getUserCommand() {
        return userCommand;
    }
}
