/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.controllers;

import com.sg.rest.operationstatus.CompleteSignupStatus;
import com.sg.rest.http.CustomHeaders;
import com.sg.rest.operationstatus.InstallStatus;
import com.sg.rest.apipath.RequestPath;
import com.sg.domain.service.SgService;
import com.sg.dto.request.SigninDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.sg.rest.operationstatus.SigninStatus;
import com.sg.rest.operationstatus.SignupStatus;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.dto.response.OperationStatusDto;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.SignupDto;
import com.sg.rest.mail.service.SgMailService;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInstallationAlreadyCompletedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.rest.security.SgRestUser;
import com.sg.rest.webtoken.WebTokenService;
import com.sg.rest.webtoken.TokenExpirationStandardDurations;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.joda.time.Instant;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tarasev
 */
@RestController
public class SigninSignupController {

    @Autowired
    private SgMailService mailService;

    @Autowired
    private SgService service;

    @Autowired
    private WebTokenService securityService;

    @RequestMapping(value = RequestPath.REQUEST_INSTALL, method = RequestMethod.GET)
    public OperationStatusDto signupUser() {
        OperationStatusDto result = new OperationStatusDto();
        result.setStatus(InstallStatus.STATUS_SUCCESS);
        try {
            service.install();
        } catch (SgInstallationAlreadyCompletedException e) {
            result.setStatus(InstallStatus.STATUS_ALREADY_COMPLETED);
        }
        return result;
    }

    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_USER, method = RequestMethod.POST)
    public OperationStatusDto signupUser(@Valid @RequestBody SignupDto dto, HttpServletResponse response) {
        OperationStatusDto result = new OperationStatusDto();
        result.setStatus(SignupStatus.STATUS_SUCCESS);
        try {
            service.signupUser(dto);

        } catch (SgSignupAlreadyCompletedException e) {
            result.setStatus(SignupStatus.STATUS_EMAIL_ALREADY_REGISTERED);
            return result;
        } catch (SgSignupForRegisteredButNonVerifiedEmailException e) {
            result.setStatus(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT);
        }
        String token = securityService.generateToken(service.getAccountId(dto.getEmail()), Instant.now(), TokenExpirationStandardDurations.EMAIL_TOKEN_EXPIRATION_DURATION);
        mailService.sendEmailVerificationEmail(token, dto.getEmail());
        return result;
    }

    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_ADMIN_USER, method = RequestMethod.POST)
    public OperationStatusDto signupAdmin(@Valid @RequestBody SignupDto dto, HttpServletResponse response) throws IOException {
        OperationStatusDto result = new OperationStatusDto();
        result.setStatus(SignupStatus.STATUS_SUCCESS);
        try {
            service.signupAdmin(dto);

        } catch (SgSignupAlreadyCompletedException e) {
            result.setStatus(SignupStatus.STATUS_EMAIL_ALREADY_REGISTERED);
            return result;
        } catch (SgSignupForRegisteredButNonVerifiedEmailException e) {
            result.setStatus(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT);
        }
        String token = securityService.generateToken(service.getAccountId(dto.getEmail()), Instant.now(), TokenExpirationStandardDurations.EMAIL_TOKEN_EXPIRATION_DURATION);
        mailService.sendEmailVerificationEmail(token, dto.getEmail());
        return result;
    }

    @RequestMapping(value = RequestPath.REQUEST_COMPLETE_SIGNUP, method = RequestMethod.POST)
    public OperationStatusDto completeSignup(@Valid @RequestBody CompleteSignupDto dto) {
        //http://blog.awnry.com/post/16187851956/spring-mvc-get-the-logged-in-userdetails-from-your
        SgRestUser user = (SgRestUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        OperationStatusDto attemptResult = new OperationStatusDto();

        try {
            service.completeSignup(user.getAccountId(), dto);
        } catch (SgAccountNotFoundException e) {
            attemptResult.setStatus(CompleteSignupStatus.STATUS_ACCOUNT_NOT_FOUND);
            return attemptResult;
        } catch (SgSignupAlreadyCompletedException ex) {
            attemptResult.setStatus(CompleteSignupStatus.STATUS_ALREADY_COMPLETED);
            return attemptResult;
        }

        attemptResult.setStatus(CompleteSignupStatus.STATUS_SUCCESS);
        return attemptResult;
    }

    @RequestMapping(value = RequestPath.REQUEST_SIGNIN, method = RequestMethod.POST)
    public OperationStatusDto signin(@Valid @RequestBody SigninDto dto, HttpServletResponse response) throws IOException {
        OperationStatusDto result = new OperationStatusDto();
        result.setStatus(SigninStatus.STATUS_SUCCESS);

        try {
            service.signIn(dto);
            String token = securityService.generateToken(service.getAccountId(dto.getEmail()), Instant.now(), TokenExpirationStandardDurations.EMAIL_TOKEN_EXPIRATION_DURATION);
            response.setHeader(CustomHeaders.X_AUTH_TOKEN, token);
        } catch (SgAccountNotFoundException e) {
            result.setStatus(SigninStatus.STATUS_USER_NOT_FOUND);
        } catch (SgInvalidPasswordException e) {
            result.setStatus(SigninStatus.STATUS_WRONG_PASSWORD);
        } catch (SgEmailNonVerifiedException e) {
            result.setStatus(SigninStatus.STATUS_EMAIL_NOT_VERIFIED);
        }

        return result;
    }

}
