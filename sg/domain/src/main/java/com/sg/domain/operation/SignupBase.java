/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.operation;

import com.sg.domail.vo.Permissions;
import com.sg.domain.ar.Account;
import com.sg.domain.enumerations.Role;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.operation.SignupOperationBase;
import com.sg.dto.operation.response.SignupCommandResponse;
import com.sg.dto.operation.status.SignupStatus;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public abstract class SignupBase {

    protected final TokenEmailPublisher tokenEmailPublisher;
    protected final AccountRepository accountRepository;

    public SignupBase(TokenEmailPublisher tokenEmailPublisher, AccountRepository accountRepository) {
        this.tokenEmailPublisher = tokenEmailPublisher;
        this.accountRepository = accountRepository;
    }

    protected SignupCommandResponse handleSignup(SignupOperationBase command, Set<Role> roles) {
        if (command.getEmail() == null || command.getEmail().isEmpty() || command.getPassword() == null || command.getPassword().isEmpty()) {
            throw new IllegalArgumentException();
        }
        Account account = accountRepository.findByEmail(command.getEmail());
        if (account != null) {
            return new SignupCommandResponse(SignupStatus.STATUS_EMAIL_ALREADY_REGISTERED);
        }
        Permissions permissions = new Permissions();
        permissions.addRoles(roles);
        account = new Account(command.getEmail(), command.getPassword(), permissions);
        accountRepository.save(account);
        tokenEmailPublisher.sendVerificationEmail(account.getId().getId(), command.getEmail());
        return new SignupCommandResponse(SignupStatus.STATUS_SUCCESS);
    }

}
