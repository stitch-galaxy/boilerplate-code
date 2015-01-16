/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.operation;

import com.sg.domain.ar.Account;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.operation.ResendVerificationEmailOperation;
import com.sg.dto.operation.response.ResendVerificationEmailCommandResponse;
import com.sg.dto.operation.status.ResendVerificationEmailStatus;

/**
 *
 * @author tarasev
 */
public class ResendVerificationEmail implements OperationExecutor<ResendVerificationEmailCommandResponse, ResendVerificationEmailOperation> {

    private final TokenEmailPublisher tokenEmailPublisher;
    private final AccountRepository accountRepository;

    public ResendVerificationEmail(TokenEmailPublisher tokenEmailPublisher, AccountRepository accountRepository) {
        this.tokenEmailPublisher = tokenEmailPublisher;
        this.accountRepository = accountRepository;
    }

    @Override
    public ResendVerificationEmailCommandResponse handle(ResendVerificationEmailOperation command) {
        if (command.getEmail() == null || command.getEmail().isEmpty()) {
            throw new IllegalArgumentException();
        }
        Account account = accountRepository.findByEmail(command.getEmail());
        if (account == null) {
            return new ResendVerificationEmailCommandResponse(ResendVerificationEmailStatus.STATUS_EMAIL_NOT_REGISTERED);
        }
        if (account.getSiteAccount().isEmailVerified()) {
            return new ResendVerificationEmailCommandResponse(ResendVerificationEmailStatus.STATUS_EMAIL_ALREADY_VERIFIED);
        }
        tokenEmailPublisher.sendVerificationEmail(account.getId().getId(), command.getEmail());
        return new ResendVerificationEmailCommandResponse(ResendVerificationEmailStatus.STATUS_SUCCESS);
    }
}
