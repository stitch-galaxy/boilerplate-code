/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.spring;

import com.sg.domain.request.GetAccountRolesRequestHandler;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tarasev
 */
@Configuration
public class DomainComponentsMockContext {

    @Bean
    public GetAccountRolesRequestHandler getAccountRolesRequestHandler() {
        return Mockito.mock(GetAccountRolesRequestHandler.class);
    }
}
