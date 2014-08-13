/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.controllers;

import com.sg.dto.UserDto;
import com.sg.domain.service.SgService;
import com.sg.dto.SigninDto;
import com.sg.dto.SinginAttempthResultDto;
import com.sg.enumerations.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sg.enumerations.SigninStatus;
import com.sg.sg_rest_api.security.AuthToken;
import com.sg.sg_rest_api.security.Security;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author tarasev
 */
@Controller
public class SigninSignupController {

    public static final String USER_NOT_REGISTERED = "User not registered";
    public static final String INCORRECT_PASSWORD = "Wrong password";
    
    @Autowired
    SgService service;
    
    @Autowired
    Security security;

    @RequestMapping(value = RequestPath.REQUEST_SIGNIN, method = RequestMethod.POST)
    public @ResponseBody
    SinginAttempthResultDto signin(@RequestBody SigninDto dto) throws IOException {
        SinginAttempthResultDto result = new SinginAttempthResultDto();
        UserDto userDto = service.getUserByEmail(dto.getEmail());
        if (userDto == null) {
            result.setStatus(SigninStatus.STATUS_USER_NOT_FOUND);
            result.setMessage(USER_NOT_REGISTERED);
        } else {
            if (!userDto.getPassword().equals(dto.getPassword())) {
                result.setStatus(SigninStatus.STATUS_WRONG_PASSWORD);
                result.setMessage(INCORRECT_PASSWORD);
            }
            else{
                result.setStatus(SigninStatus.STATUS_SUCCESS);
                
                AuthToken token = new AuthToken();
                token.setEmail(userDto.getEmail());
                List<String> authorities = new ArrayList<String>();
                for (String r : userDto.getRoles())
                {
                    authorities.add(Roles.ROLE_AUTHORITY_PREFIX + r);
                }
                
                token.setAuthorities(authorities);
                //TODO: add expiration
                
                result.setAuthToken(security.getTokenString(token));
            }
        }
        return result;
    }

}
