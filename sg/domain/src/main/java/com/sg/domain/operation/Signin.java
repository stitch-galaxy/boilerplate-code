/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.operation;

import com.sg.domain.ar.Account;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.operation.SigninOperation;
import com.sg.dto.operation.response.SigninCommandResponse;
import com.sg.dto.operation.status.SigninStatus;

/**
 *
 * @author tarasev
 */
public class Signin implements OperationExecutor<SigninCommandResponse, SigninOperation> {

    private final AccountRepository accountRepository;
    private final TokenComponent securityComponent;

    public Signin(AccountRepository accountRepository, TokenComponent securityComponent) {
        this.accountRepository = accountRepository;
        this.securityComponent = securityComponent;
    }

    @Override
    public SigninCommandResponse handle(SigninOperation command) {
        if (command.getEmail() == null || command.getEmail().isEmpty() || command.getPassword() == null || command.getPassword().isEmpty())
            throw new IllegalArgumentException();
        Account account = accountRepository.findByEmail(command.getEmail());
        if (account == null) {
            return new SigninCommandResponse(SigninStatus.STATUS_ACCOUNT_NOT_FOUND);
        }
        if (account.getSiteAccount() == null) {
            return new SigninCommandResponse(SigninStatus.STATUS_ACCOUNT_NOT_LINKED_TO_EMAIL);
        }
        if (account.getSiteAccount().isEmailVerified()) {
            return new SigninCommandResponse(SigninStatus.STATUS_EMAIL_NOT_VERIFIED);
        }
        if (!account.getSiteAccount().isPasswordCorrect(command.getPassword()))
            return new SigninCommandResponse(SigninStatus.STATUS_WRONG_PASSWORD);
            
        return new SigninCommandResponse(securityComponent.generateSessionToken(account.getId().getId()));
    }

}
