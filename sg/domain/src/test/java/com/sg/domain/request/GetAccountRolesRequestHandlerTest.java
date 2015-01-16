/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.request;

import com.sg.domain.handler.request.GetAccountRolesRequestHandler;
import com.sg.domail.vo.Permissions;
import com.sg.domain.ar.Account;
import com.sg.domain.enumerations.Role;
import com.sg.domain.repository.AccountRepository;
import com.sg.dto.enumerations.GetAccountRolesStatus;
import com.sg.dto.request.cqrs.GetAccountRolesRequest;
import com.sg.dto.request.response.GetAccountRolesResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
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
    
    @Test
    public void testException()
    {
        when(accountRepository.findOne(1l)).thenReturn(null);
        
        GetAccountRolesRequest dto = new GetAccountRolesRequest(1l);
        GetAccountRolesResponse response = handler.handle(dto);
        assertEquals(GetAccountRolesStatus.STATUS_ACCOUNT_NOT_FOUND, response.getStatus());
        assertEquals(null, response.getRoles());
    }
    
    @Test
    public void testRequest()
    {
        Permissions permissions = new Permissions();
        permissions.addRole(Role.USER);
        permissions.addRole(Role.ADMIN);
        Account account = new Account("user@gmail.com", "password", permissions);
        when(accountRepository.findOne(1l)).thenReturn(account);
        
        GetAccountRolesRequest dto = new GetAccountRolesRequest(1l);
        GetAccountRolesResponse response = handler.handle(dto);
        assertEquals(GetAccountRolesStatus.STATUS_SUCCESS, response.getStatus());
        assertThat(response.getRoles(), hasSize(2));
        assertThat(response.getRoles(), Matchers.containsInAnyOrder(Role.USER, Role.ADMIN));
    }
    
}
