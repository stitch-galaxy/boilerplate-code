/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.handler.command;

import com.sg.domain.ar.Account;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.command.ResendVerificationEmailCommand;
import com.sg.dto.command.response.ResendVerificationEmailCommandStatus;
import com.sg.dto.enumerations.ResendVerificationEmailStatus;

/**
 *
 * @author tarasev
 */
public class ResendVerificationEmailCommandHandler implements CommandHandler<ResendVerificationEmailCommandStatus, ResendVerificationEmailCommand> {

    private final TokenEmailPublisher tokenEmailPublisher;
    private final AccountRepository accountRepository;

    public ResendVerificationEmailCommandHandler(TokenEmailPublisher tokenEmailPublisher, AccountRepository accountRepository) {
        this.tokenEmailPublisher = tokenEmailPublisher;
        this.accountRepository = accountRepository;
    }

    @Override
    public ResendVerificationEmailCommandStatus handle(ResendVerificationEmailCommand command) {
        if (command.getEmail() == null || command.getEmail().isEmpty()) {
            throw new IllegalArgumentException();
        }
        Account account = accountRepository.findByEmail(command.getEmail());
        if (account == null) {
            return new ResendVerificationEmailCommandStatus(ResendVerificationEmailStatus.STATUS_EMAIL_NOT_REGISTERED);
        }
        if (account.getSiteAccount().isEmailVerified()) {
            return new ResendVerificationEmailCommandStatus(ResendVerificationEmailStatus.STATUS_EMAIL_ALREADY_VERIFIED);
        }
        tokenEmailPublisher.sendVerificationEmail(account.getId().getId(), command.getEmail());
        return new ResendVerificationEmailCommandStatus(ResendVerificationEmailStatus.STATUS_SUCCESS);
    }
}
