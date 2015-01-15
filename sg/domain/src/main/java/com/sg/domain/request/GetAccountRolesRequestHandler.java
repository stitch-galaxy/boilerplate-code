/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.request;

import com.sg.domain.ar.Account;
import com.sg.domain.exception.SgAccountNotFoundException;
import com.sg.domain.repository.cqrs.AccountRepository;
import com.sg.dto.request.cqrs.GetAccountRolesRequest;
import com.sg.dto.request.response.cqrs.GetAccountRolesResponse;

/**
 *
 * @author tarasev
 */
public class GetAccountRolesRequestHandler implements RequestHandler<GetAccountRolesResponse, GetAccountRolesRequest> {
    
    private final AccountRepository accountRepository;
    
    public GetAccountRolesRequestHandler(AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }
    
    @Override
    public GetAccountRolesResponse handle(GetAccountRolesRequest dto) throws SgAccountNotFoundException
    {
        Account account = accountRepository.findOne(dto.getAccountId());
        if (account == null) {
            throw new SgAccountNotFoundException(dto.getAccountId());
        }
        GetAccountRolesResponse response = new GetAccountRolesResponse(account.getPermissions().getRoles());
        return response;
    }
}
