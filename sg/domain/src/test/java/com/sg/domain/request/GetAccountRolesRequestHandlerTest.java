/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.request;

import com.sg.domail.vo.Permissions;
import com.sg.domain.ar.Account;
import com.sg.domain.enumerations.Role;
import com.sg.domain.exception.SgAccountNotFoundException;
import com.sg.domain.repository.cqrs.AccountRepository;
import com.sg.dto.request.cqrs.GetAccountRolesRequest;
import com.sg.dto.request.response.cqrs.GetAccountRolesResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 *
 * @author tarasev
 */
public class GetAccountRolesRequestHandlerTest {
    private static AccountRepository accountRepository;
    private static GetAccountRolesRequestHandler handler;
    
    @BeforeClass
    public static void setupClass() {
        accountRepository = Mockito.mock(AccountRepository.class);
        handler = new GetAccountRolesRequestHandler(accountRepository);
    }

    @Before
    public void setup() {
        Mockito.reset(accountRepository);
    }
    
    @Test(expected = SgAccountNotFoundException.class)
    public void testException()
    {
        when(accountRepository.findOne(1l)).thenReturn(null);
        
        GetAccountRolesRequest dto = new GetAccountRolesRequest();
        dto.setAccountId(1l);
        handler.handle(dto);
    }
    
    @Test
    public void testRequest()
    {
        Permissions permissions = new Permissions().addRole(Role.USER).addRole(Role.ADMIN);
        Account account = new Account("user@gmail.com", "password", permissions);
        when(accountRepository.findOne(1l)).thenReturn(account);
        
        GetAccountRolesRequest dto = new GetAccountRolesRequest();
        dto.setAccountId(1l);
        GetAccountRolesResponse response = handler.handle(dto);
        assertThat(response.getRoles(), hasSize(2));
        assertThat(response.getRoles(), Matchers.containsInAnyOrder(Role.USER, Role.ADMIN));
    }
    
}
