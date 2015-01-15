/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.spring.test;

import com.sg.domain.handler.command.CompleteSignupCommandHandler;
import com.sg.domain.handler.command.ResendVerificationEmailCommandHandler;
import com.sg.domain.handler.command.SigninCommandHandler;
import com.sg.domain.handler.command.SignupAdminCommandHandler;
import com.sg.domain.handler.command.SignupUserCommandHandler;
import com.sg.domain.handler.command.TokenComponent;
import com.sg.domain.handler.command.TokenEmailPublisher;
import com.sg.domain.handler.request.GetAccountRolesRequestHandler;
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

    @Bean
    public CompleteSignupCommandHandler completeSignupCommandHandler() {
        return Mockito.mock(CompleteSignupCommandHandler.class);
    }

    @Bean
    public ResendVerificationEmailCommandHandler resendVerificationEmailCommandHandler() {
        return Mockito.mock(ResendVerificationEmailCommandHandler.class);
    }

    @Bean
    public SignupAdminCommandHandler signupAdminCommandHandler() {
        return Mockito.mock(SignupAdminCommandHandler.class);
    }

    @Bean
    public SignupUserCommandHandler signupUserCommandHandler() {
        return Mockito.mock(SignupUserCommandHandler.class);
    }
    
    @Bean
    public SigninCommandHandler signinCommandHandler() {
        return Mockito.mock(SigninCommandHandler.class);
    }

    @Bean
    public TokenComponent tokenComponent() {
        return Mockito.mock(TokenComponent.class);
    }

    @Bean
    public TokenEmailPublisher tokenEmailPublisher() {
        return Mockito.mock(TokenEmailPublisher.class);
    }
}
