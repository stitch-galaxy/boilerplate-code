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
import com.sg.dto.operation.status.GetAccountRolesStatus;
import com.sg.dto.operation.GetAccountRolesOperation;
import com.sg.dto.operation.response.GetAccountRolesResponse;
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
    private static GetAccountRoles handler;
    
    @BeforeClass
    public static void setupClass() {
        accountRepository = Mockito.mock(AccountRepository.class);
        handler = new GetAccountRoles(accountRepository);
    }

    @Before
    public void setup() {
        Mockito.reset(accountRepository);
    }
    
    @Test
    public void testException()
    {
        when(accountRepository.findOne(1l)).thenReturn(null);
        
        GetAccountRolesOperation dto = new GetAccountRolesOperation(1l);
        GetAccountRolesResponse response = handler.handle(dto);
        assertEquals(GetAccountRolesStatus.STATUS_ACCOUNT_NOT_FOUND, response.getStatus());
        assertEquals(null, response.getData());
    }
    
    @Test
    public void testRequest()
    {
        Permissions permissions = new Permissions();
        permissions.addRole(Role.USER);
        permissions.addRole(Role.ADMIN);
        Account account = new Account("user@gmail.com", "password", permissions);
        when(accountRepository.findOne(1l)).thenReturn(account);
        
        GetAccountRolesOperation dto = new GetAccountRolesOperation(1l);
        GetAccountRolesResponse response = handler.handle(dto);
        assertEquals(GetAccountRolesStatus.STATUS_SUCCESS, response.getStatus());
        assertThat(response.getData().getRoles(), hasSize(2));
        assertThat(response.getData().getRoles(), Matchers.containsInAnyOrder(Role.USER, Role.ADMIN));
    }
    
}
