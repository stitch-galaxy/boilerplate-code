/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.command;

import com.sg.domain.ar.Account;
import com.sg.domain.repository.cqrs.AccountRepository;
import com.sg.dto.command.cqrs.CompleteSignupCommand;
import com.sg.dto.command.response.cqrs.CompleteSignupCommandResponse;
import com.sg.dto.enumerations.CompleteSignupCommandStatus;

/**
 *
 * @author tarasev
 */
public class CompleteSignupCommandHandler implements CommandHandler<CompleteSignupCommandResponse, CompleteSignupCommand> {

    private final AccountRepository accountRepository;
    private final TokenComponent securityComponent;

    public CompleteSignupCommandHandler(AccountRepository accountRepository, TokenComponent securityComponent) {
        this.accountRepository = accountRepository;
        this.securityComponent = securityComponent;
    }

    @Override
    public CompleteSignupCommandResponse handle(CompleteSignupCommand command) {
        Account account = accountRepository.findOne(command.getAccountId());
        if (account == null) {
            return new CompleteSignupCommandResponse(CompleteSignupCommandStatus.STATUS_ACCOUNT_NOT_FOUND);
        }
        if (account.getSiteAccount() == null) {
            return new CompleteSignupCommandResponse(CompleteSignupCommandStatus.STATUS_ACCOUNT_NOT_LINKED_TO_EMAIL);
        }
        if (account.getSiteAccount().isEmailVerified()) {
            return new CompleteSignupCommandResponse(CompleteSignupCommandStatus.STATUS_ALREADY_COMPLETED);
        }
        account.getSiteAccount().verifyEmail();
        accountRepository.save(account);
        return new CompleteSignupCommandResponse(securityComponent.generateSessionToken(account.getId().getId()));
    }
}
