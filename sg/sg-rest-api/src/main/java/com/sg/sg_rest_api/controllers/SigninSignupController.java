/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.controllers;

import com.sg.constants.RequestPath;
import com.sg.dto.AccountDto;
import com.sg.domain.service.SgService;
import com.sg.dto.SigninDto;
import com.sg.dto.SinginAttempthResultDto;
import com.sg.constants.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sg.constants.SigninStatus;
import com.sg.constants.SignupStatus;
import com.sg.constants.TokenExpirationType;
import com.sg.dto.SignupDto;
import com.sg.dto.SingupAttempthResultDto;
import com.sg.sg_rest_api.mail.MailService;
import com.sg.sg_rest_api.security.AuthToken;
import com.sg.sg_rest_api.security.Security;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tarasev
 */
@Controller
public class SigninSignupController {

    @Autowired
    MailService mailService;
    
    @Autowired
    SgService service;

    @Autowired
    Security security;

    @RequestMapping(value = RequestPath.REQUEST_SIGNIN, method = RequestMethod.POST)
    public @ResponseBody
    SinginAttempthResultDto signin(@RequestBody SigninDto dto) throws IOException {
        SinginAttempthResultDto result = new SinginAttempthResultDto();
        AccountDto accountDto = service.getUserByEmail(dto.getEmail());
        if (accountDto == null) {
            result.setStatus(SigninStatus.STATUS_USER_NOT_FOUND);
        } else {
            if (!accountDto.getEmailVerified()) {
                result.setStatus(SigninStatus.STATUS_EMAIL_NOT_VERIFIED);
            } else if (!accountDto.getPassword().equals(dto.getPassword())) {
                result.setStatus(SigninStatus.STATUS_WRONG_PASSWORD);
            } else {
                result.setStatus(SigninStatus.STATUS_SUCCESS);

                AuthToken token = new AuthToken(accountDto, TokenExpirationType.USER_SESSION_TOKEN);
                
                result.setAuthToken(security.getTokenString(token));
            }
        }
        return result;
    }

    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_USER, method = RequestMethod.POST)
    public @ResponseBody
    SingupAttempthResultDto signupUser(@RequestBody SignupDto dto) throws IOException {
        SingupAttempthResultDto result = new SingupAttempthResultDto();
        AccountDto accountDto = service.getUserByEmail(dto.getEmail());
        if (accountDto != null) {
            if (accountDto.getEmailVerified() == Boolean.TRUE)
            {
                result.setStatus(SignupStatus.STATUS_EMAIL_ALREADY_REGISTERED);
            }
            else
            {
                AuthToken authToken = new AuthToken(accountDto, TokenExpirationType.NEVER_EXPIRES);
                
                
                mailService.sendEmailVerificationEmail(security.getTokenString(authToken), dto.getEmail());
                result.setStatus(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT);
            }
        } else {
            accountDto = new AccountDto();
            accountDto.setEmail(dto.getEmail());
            accountDto.setEmailVerified(Boolean.FALSE);
            accountDto.setUserFirstName(dto.getUserFirstName());
            accountDto.setUserLastName(dto.getUserLastName());
            accountDto.setUserBirthDate(dto.getUserBirthDate());
            List<String> roles = new ArrayList<String>();
            roles.add(Roles.ROLE_USER);
            accountDto.setRoles(roles);
            
            AuthToken authToken = new AuthToken(accountDto, TokenExpirationType.NEVER_EXPIRES);
            
            mailService.sendEmailVerificationEmail(security.getTokenString(authToken), dto.getEmail());
            
            service.create(accountDto);
            result.setStatus(SignupStatus.STATUS_SUCCESS);
        }
        return result;
    } 
    
}
