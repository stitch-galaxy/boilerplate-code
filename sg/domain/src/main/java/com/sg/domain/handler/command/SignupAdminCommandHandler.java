/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.handler.command;

import com.sg.domain.enumerations.Role;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.command.SignupAdminCommand;
import com.sg.dto.command.response.SignupCommandStatus;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class SignupAdminCommandHandler extends SignupBaseCommandHandler implements CommandHandler<SignupCommandStatus, SignupAdminCommand> {

    public SignupAdminCommandHandler(TokenEmailPublisher tokenEmailPublisher, AccountRepository accountRepository) {
        super(tokenEmailPublisher, accountRepository);
    }

    @Override
    public SignupCommandStatus handle(SignupAdminCommand command) {
        Set<Role> roles = EnumSet.noneOf(Role.class);
        roles.add(Role.USER);
        roles.add(Role.ADMIN);
        return handleSignup(command, roles);
    }
}
