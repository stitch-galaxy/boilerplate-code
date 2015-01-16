/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.operation;

import com.sg.domain.enumerations.Role;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.operation.SignupAdminOperation;
import com.sg.dto.operation.response.SignupCommandResponse;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class SignupAdmin extends SignupBase implements OperationExecutor<SignupCommandResponse, SignupAdminOperation> {

    public SignupAdmin(TokenEmailPublisher tokenEmailPublisher, AccountRepository accountRepository) {
        super(tokenEmailPublisher, accountRepository);
    }

    @Override
    public SignupCommandResponse handle(SignupAdminOperation command) {
        Set<Role> roles = EnumSet.noneOf(Role.class);
        roles.add(Role.USER);
        roles.add(Role.ADMIN);
        return handleSignup(command, roles);
    }
}
