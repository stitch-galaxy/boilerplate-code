/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.spring;

import com.sg.domain.handler.command.CompleteSignupCommandHandler;
import com.sg.domain.handler.command.ResendVerificationEmailCommandHandler;
import com.sg.domain.handler.command.SigninCommandHandler;
import com.sg.domain.handler.command.SignupAdminCommandHandler;
import com.sg.domain.handler.command.SignupUserCommandHandler;
import com.sg.domain.handler.command.TokenComponent;
import com.sg.domain.handler.command.TokenEmailPublisher;
import com.sg.domain.handler.command.GetAccountRolesRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tarasev
 */
@Configuration
public class DomainComponentsContext {

    @Bean
    public GetAccountRolesRequestHandler getAccountRolesRequestHandler() {
        return new GetAccountRolesRequestHandler(null);
    }

    @Bean
    public CompleteSignupCommandHandler completeSignupCommandHandler() {
        return new CompleteSignupCommandHandler(null, null);
    }

    @Bean
    public ResendVerificationEmailCommandHandler resendVerificationEmailCommandHandler() {
        return new ResendVerificationEmailCommandHandler(null, null);
    }

    @Bean
    public SignupAdminCommandHandler signupAdminCommandHandler() {
        return new SignupAdminCommandHandler(null, null);
    }

    @Bean
    public SignupUserCommandHandler signupUserCommandHandler() {
        return new SignupUserCommandHandler(null, null);
    }

    @Bean
    public SigninCommandHandler signinCommandHandler() {
        return new SigninCommandHandler(null, null);
    }

    @Bean
    public TokenComponent tokenComponent() {
        return new TokenComponent(null);
    }

    @Bean
    public TokenEmailPublisher tokenEmailPublisher() {
        return new TokenEmailPublisher(null, null);
    }
}
