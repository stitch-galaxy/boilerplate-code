/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.operation;

import com.sg.domain.ar.Account;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.operation.CompleteSignupOperation;
import com.sg.dto.operation.response.CompleteSignupResponse;
import com.sg.dto.operation.status.CompleteSignupCommandStatus;

/**
 *
 * @author tarasev
 */
public class CompleteSignup implements OperationExecutor<CompleteSignupResponse, CompleteSignupOperation> {

    private final AccountRepository accountRepository;
    private final TokenComponent securityComponent;

    public CompleteSignup(AccountRepository accountRepository, TokenComponent securityComponent) {
        this.accountRepository = accountRepository;
        this.securityComponent = securityComponent;
    }

    @Override
    public CompleteSignupResponse handle(CompleteSignupOperation command) {
        Account account = accountRepository.findOne(command.getAccountId());
        if (account == null) {
            return new CompleteSignupResponse(CompleteSignupCommandStatus.STATUS_ACCOUNT_NOT_FOUND);
        }
        if (account.getSiteAccount() == null) {
            return new CompleteSignupResponse(CompleteSignupCommandStatus.STATUS_ACCOUNT_NOT_LINKED_TO_EMAIL);
        }
        if (account.getSiteAccount().isEmailVerified()) {
            return new CompleteSignupResponse(CompleteSignupCommandStatus.STATUS_ALREADY_COMPLETED);
        }
        account.getSiteAccount().verifyEmail();
        accountRepository.save(account);
        return new CompleteSignupResponse(securityComponent.generateSessionToken(account.getId().getId()));
    }
}
