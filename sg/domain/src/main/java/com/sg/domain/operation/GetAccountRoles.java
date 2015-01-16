/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.operation;

import com.sg.domain.ar.Account;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.operation.status.GetAccountRolesStatus;
import com.sg.dto.operation.GetAccountRolesOperation;
import com.sg.dto.operation.response.GetAccountRolesResponse;

/**
 *
 * @author tarasev
 */
public class GetAccountRoles implements OperationExecutor<GetAccountRolesResponse, GetAccountRolesOperation> {

    private final AccountRepository accountRepository;

    public GetAccountRoles(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public GetAccountRolesResponse handle(GetAccountRolesOperation dto) {
        Account account = accountRepository.findOne(dto.getAccountId());
        if (account == null) {
            return new GetAccountRolesResponse(GetAccountRolesStatus.STATUS_ACCOUNT_NOT_FOUND);
        }
        GetAccountRolesResponse response = new GetAccountRolesResponse(account.getPermissions().getRoles());
        return response;
    }
}
