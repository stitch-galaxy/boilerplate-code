/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.spring.test;

import com.sg.domain.operation.CompleteSignup;
import com.sg.domain.operation.ResendVerificationEmail;
import com.sg.domain.operation.Signin;
import com.sg.domain.operation.SignupAdmin;
import com.sg.domain.operation.SignupUser;
import com.sg.domain.operation.TokenComponent;
import com.sg.domain.operation.TokenEmailPublisher;
import com.sg.domain.operation.GetAccountRoles;
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
    public GetAccountRoles getAccountRolesRequestHandler() {
        return Mockito.mock(GetAccountRoles.class);
    }

    @Bean
    public CompleteSignup completeSignupCommandHandler() {
        return Mockito.mock(CompleteSignup.class);
    }

    @Bean
    public ResendVerificationEmail resendVerificationEmailCommandHandler() {
        return Mockito.mock(ResendVerificationEmail.class);
    }

    @Bean
    public SignupAdmin signupAdminCommandHandler() {
        return Mockito.mock(SignupAdmin.class);
    }

    @Bean
    public SignupUser signupUserCommandHandler() {
        return Mockito.mock(SignupUser.class);
    }
    
    @Bean
    public Signin signinCommandHandler() {
        return Mockito.mock(Signin.class);
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
