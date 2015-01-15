/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.command;

import com.sg.domain.enumerations.Role;
import com.sg.domain.repository.cqrs.AccountRepository;
import com.sg.dto.command.cqrs.SignupUserCommand;
import com.sg.dto.command.response.cqrs.SignupCommandStatus;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class SignupUserCommandHandler extends SignupBaseCommandHandler implements CommandHandler<SignupCommandStatus, SignupUserCommand>  {

    public SignupUserCommandHandler(TokenEmailPublisher tokenEmailPublisher, AccountRepository accountRepository) {
        super(tokenEmailPublisher, accountRepository);
    }

    @Override
    public SignupCommandStatus handle(SignupUserCommand command) {
        Set<Role> roles = EnumSet.noneOf(Role.class);
        roles.add(Role.USER);
        return handleSignup(command, roles);
    }
}
