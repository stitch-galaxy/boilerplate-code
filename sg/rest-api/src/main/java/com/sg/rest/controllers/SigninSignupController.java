/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.controllers;

import com.sg.domain.command.CommandHandler;
import com.sg.rest.apipath.RequestPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.sg.dto.command.cqrs.CompleteSignupCommand;
import com.sg.dto.command.cqrs.CompleteSignupUserCommand;
import com.sg.dto.command.cqrs.SigninCommand;
import com.sg.dto.command.cqrs.SignupAdminCommand;
import com.sg.dto.command.cqrs.SignupUserCommand;
import com.sg.dto.command.response.cqrs.CompleteSignupCommandResponse;
import com.sg.dto.command.response.cqrs.SigninCommandResponse;
import com.sg.dto.command.response.cqrs.SignupCommandStatus;
import com.sg.rest.security.SgRestUser;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tarasev
 */
@RestController
public class SigninSignupController {

    @Autowired
    private CommandHandler<SignupCommandStatus, SignupUserCommand> signupUserCommandHandler;

    @Autowired
    private CommandHandler<SignupCommandStatus, SignupAdminCommand> signupAdminCommandHandler;

    @Autowired
    CommandHandler<CompleteSignupCommandResponse, CompleteSignupCommand> completeSignupHandler;

    @Autowired
    CommandHandler<SigninCommandResponse, SigninCommand> singinCommandHandler;

//    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_USER, method = RequestMethod.POST)
//    public SignupCommandStatus signupUser(@Valid @RequestBody SignupUserCommand dto) {
//        return signupUserCommandHandler.handle(dto);
//    }
//
//    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_ADMIN_USER, method = RequestMethod.POST)
//    public SignupCommandStatus signupAdmin(@Valid @RequestBody SignupAdminCommand dto) {
//        return signupAdminCommandHandler.handle(dto);
//    }
//
//    @RequestMapping(value = RequestPath.REQUEST_COMPLETE_SIGNUP, method = RequestMethod.POST)
//    public CompleteSignupCommandResponse completeSignup(@Valid @RequestBody CompleteSignupUserCommand dto) {
//        //http://blog.awnry.com/post/16187851956/spring-mvc-get-the-logged-in-userdetails-from-your
//        SgRestUser user = (SgRestUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        CompleteSignupCommand command = new CompleteSignupCommand(user.getAccountId(), dto);
//        return completeSignupHandler.handle(command);
//    }
//
//    @RequestMapping(value = RequestPath.REQUEST_SIGNIN, method = RequestMethod.POST)
//    public SigninCommandResponse signin(@Valid @RequestBody SigninCommand dto) throws IOException {
//        return singinCommandHandler.handle(dto);
//    }

}
