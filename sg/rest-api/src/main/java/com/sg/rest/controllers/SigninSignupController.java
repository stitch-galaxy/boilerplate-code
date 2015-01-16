/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.controllers;

import com.sg.domain.operation.OperationExecutor;
import com.sg.rest.path.RequestPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.sg.dto.operation.CompleteSignupOperation;
import com.sg.dto.operation.CompleteSignupParameters;
import com.sg.dto.operation.SigninOperation;
import com.sg.dto.operation.SignupAdminOperation;
import com.sg.dto.operation.SignupUserOperation;
import com.sg.dto.operation.response.CompleteSignupResponse;
import com.sg.dto.operation.response.SigninCommandResponse;
import com.sg.dto.operation.response.SignupCommandResponse;
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
    private OperationExecutor<SignupCommandResponse, SignupUserOperation> signupUserCommandHandler;

    @Autowired
    private OperationExecutor<SignupCommandResponse, SignupAdminOperation> signupAdminCommandHandler;

    @Autowired
    OperationExecutor<CompleteSignupResponse, CompleteSignupOperation> completeSignupHandler;

    @Autowired
    OperationExecutor<SigninCommandResponse, SigninOperation> singinCommandHandler;

    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_USER, method = RequestMethod.POST)
    public SignupCommandResponse signupUser(@Valid @RequestBody SignupUserOperation dto) {
        return signupUserCommandHandler.handle(dto);
    }

    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_ADMIN_USER, method = RequestMethod.POST)
    public SignupCommandResponse signupAdmin(@Valid @RequestBody SignupAdminOperation dto) {
        return signupAdminCommandHandler.handle(dto);
    }

    @RequestMapping(value = RequestPath.REQUEST_COMPLETE_SIGNUP, method = RequestMethod.POST)
    public CompleteSignupResponse completeSignup(@Valid @RequestBody CompleteSignupParameters dto) {
        //http://blog.awnry.com/post/16187851956/spring-mvc-get-the-logged-in-userdetails-from-your
        SgRestUser user = (SgRestUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CompleteSignupOperation command = new CompleteSignupOperation(user.getAccountId(), dto);
        return completeSignupHandler.handle(command);
    }

    @RequestMapping(value = RequestPath.REQUEST_SIGNIN, method = RequestMethod.POST)
    public SigninCommandResponse signin(@Valid @RequestBody SigninOperation dto) throws IOException {
        return singinCommandHandler.handle(dto);
    }

}
