/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.command;

import com.sg.domain.enumerations.Role;
import com.sg.domain.repository.cqrs.AccountRepository;
import com.sg.dto.command.cqrs.Command;
import com.sg.dto.command.cqrs.SignupAdminCommand;
import com.sg.dto.command.response.cqrs.CommandResponse;
import com.sg.dto.command.response.cqrs.SignupCommandStatus;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class SignupAdminCommandHandler extends SignupBaseCommandHandler implements CommandHandler {

    public SignupAdminCommandHandler(TokenEmailPublisher tokenEmailPublisher, AccountRepository accountRepository) {
        super(tokenEmailPublisher, accountRepository);
    }

    public SignupCommandStatus handle(SignupAdminCommand command) {
        Set<Role> roles = EnumSet.noneOf(Role.class);
        roles.add(Role.USER);
        roles.add(Role.ADMIN);
        return handleSignup(command, roles);
    }

    @Override
    public CommandResponse handle(Command command) {
        return handle((SignupAdminCommand) command);
    }

    @Override
    public Type getCommandType() {
        return SignupAdminCommand.class;
    }
    
}
