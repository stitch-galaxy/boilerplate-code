/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.spring;

import com.sg.domain.operation.CompleteSignup;
import com.sg.domain.operation.ResendVerificationEmail;
import com.sg.domain.operation.Signin;
import com.sg.domain.operation.SignupAdmin;
import com.sg.domain.operation.SignupUser;
import com.sg.domain.operation.TokenComponent;
import com.sg.domain.operation.TokenEmailPublisher;
import com.sg.domain.operation.GetAccountRoles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tarasev
 */
@Configuration
public class DomainComponentsContext {

    @Bean
    public GetAccountRoles getAccountRolesRequestHandler() {
        return new GetAccountRoles(null);
    }

    @Bean
    public CompleteSignup completeSignupCommandHandler() {
        return new CompleteSignup(null, null);
    }

    @Bean
    public ResendVerificationEmail resendVerificationEmailCommandHandler() {
        return new ResendVerificationEmail(null, null);
    }

    @Bean
    public SignupAdmin signupAdminCommandHandler() {
        return new SignupAdmin(null, null);
    }

    @Bean
    public SignupUser signupUserCommandHandler() {
        return new SignupUser(null, null);
    }

    @Bean
    public Signin signinCommandHandler() {
        return new Signin(null, null);
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
