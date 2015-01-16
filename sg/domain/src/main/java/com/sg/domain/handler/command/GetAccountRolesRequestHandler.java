/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.handler.command;

import com.sg.domain.ar.Account;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.enumerations.GetAccountRolesStatus;
import com.sg.dto.command.GetAccountRolesRequest;
import com.sg.dto.command.response.GetAccountRolesResponse;

/**
 *
 * @author tarasev
 */
public class GetAccountRolesRequestHandler implements CommandHandler<GetAccountRolesResponse, GetAccountRolesRequest> {

    private final AccountRepository accountRepository;

    public GetAccountRolesRequestHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public GetAccountRolesResponse handle(GetAccountRolesRequest dto) {
        Account account = accountRepository.findOne(dto.getAccountId());
        if (account == null) {
            return new GetAccountRolesResponse(GetAccountRolesStatus.STATUS_ACCOUNT_NOT_FOUND);
        }
        GetAccountRolesResponse response = new GetAccountRolesResponse(account.getPermissions().getRoles());
        return response;
    }
}
