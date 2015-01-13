/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.command;

import com.sg.domain.enumerations.Role;
import com.sg.rest.authtoken.enumerations.TokenExpirationStandardDurations;
import com.sg.dto.command.cqrs.SignupCommand;
import com.sg.dto.command.response.cqrs.SignupCommandStatus;
import com.sg.dto.enumerations.SignupStatus;
import com.sg.rest.authtoken.AuthTokenComponent;
import com.sg.rest.authtoken.Token;
import java.util.EnumSet;
import java.util.Set;
import org.joda.time.Instant;

/**
 *
 * @author tarasev
 */
public class SignupCommandHandler {
    
    AuthTokenComponent securityComponent;
    
    public SignupCommandStatus handleUserSignup(SignupCommand command)
    {
        Set<Role> roles = EnumSet.noneOf(Role.class);
        roles.add(Role.USER);
        return handleSignup(command, roles);
    }
    
    public SignupCommandStatus handleAdminSignup(SignupCommand command)
    {
        Set<Role> roles = EnumSet.noneOf(Role.class);
        roles.add(Role.USER);
        roles.add(Role.ADMIN);
        return handleSignup(command, roles);
    }
    
    private SignupCommandStatus handleSignup(SignupCommand command, Set<Role> roles)
    {
        
        return new SignupCommandStatus(SignupStatus.STATUS_SUCCESS);
    }
    
    private void sendVerificationEmail(long accountId)
    {
        Token token = new Token();
        token.setUid(String.valueOf(accountId));

        String tokenString = securityComponent.signToken(token, Instant.now(), TokenExpirationStandardDurations.EMAIL_TOKEN_EXPIRATION_DURATION);
        //send email
    }
    
    
    
    
    
    
    
    
    
//    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_USER, method = RequestMethod.POST)
//    public OperationStatusDto signupUser(@Valid @RequestBody SignupDto dto, HttpServletResponse response) {
//        
//        try {
//            service.signupUser(dto);
//
//        } catch (SgSignupAlreadyCompletedException e) {
//            result.setStatus(SignupStatus.STATUS_EMAIL_ALREADY_REGISTERED);
//            return result;
//        } catch (SgSignupForRegisteredButNonVerifiedEmailException e) {
//            result.setStatus(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT);
//        }
//        
//        mailService.sendEmailVerificationEmail(token, dto.getEmail());
//        return result;
//    }
}
