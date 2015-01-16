/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.handler.command;

import com.sg.domail.vo.Permissions;
import com.sg.domain.ar.Account;
import com.sg.domain.enumerations.Role;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.command.SignupCommand;
import com.sg.dto.command.response.SignupCommandStatus;
import com.sg.dto.enumerations.SignupStatus;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public abstract class SignupBaseCommandHandler {

    protected final TokenEmailPublisher tokenEmailPublisher;
    protected final AccountRepository accountRepository;

    public SignupBaseCommandHandler(TokenEmailPublisher tokenEmailPublisher, AccountRepository accountRepository) {
        this.tokenEmailPublisher = tokenEmailPublisher;
        this.accountRepository = accountRepository;
    }

    protected SignupCommandStatus handleSignup(SignupCommand command, Set<Role> roles) {
        if (command.getEmail() == null || command.getEmail().isEmpty() || command.getPassword() == null || command.getPassword().isEmpty()) {
            throw new IllegalArgumentException();
        }
        Account account = accountRepository.findByEmail(command.getEmail());
        if (account != null) {
            return new SignupCommandStatus(SignupStatus.STATUS_EMAIL_ALREADY_REGISTERED);
        }
        Permissions permissions = new Permissions();
        permissions.addRoles(roles);
        account = new Account(command.getEmail(), command.getPassword(), permissions);
        accountRepository.save(account);
        tokenEmailPublisher.sendVerificationEmail(account.getId().getId(), command.getEmail());
        return new SignupCommandStatus(SignupStatus.STATUS_SUCCESS);
    }

}
