/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.controllers;

import com.sg.constants.CompleteSignupStatus;
import com.sg.constants.CustomHttpHeaders;
import com.sg.constants.InstallStatus;
import com.sg.constants.RequestPath;
import com.sg.dto.response.AccountDto;
import com.sg.domain.service.SgService;
import com.sg.dto.request.SigninDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sg.constants.SigninStatus;
import com.sg.constants.SignupStatus;
import com.sg.constants.TokenExpirationType;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.dto.response.OperationStatusDto;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.SignupDto;
import com.sg.domain.service.SgMailService;
import com.sg.domain.service.AuthToken;
import com.sg.domain.service.SgCryptoService;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgCryptoException;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInstallationAlreadyCompletedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.joda.time.Instant;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author tarasev
 */
@Controller
public class SigninSignupController {

    @Autowired
    SgMailService mailService;

    @Autowired
    SgService service;

    @Autowired
    SgCryptoService security;
    
    @RequestMapping(value = RequestPath.REQUEST_INSTALL, method = RequestMethod.GET)
    public @ResponseBody
    OperationStatusDto signupUser() {
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
    public @ResponseBody
    OperationStatusDto signupUser(@Valid @RequestBody SignupDto dto, HttpServletResponse response) throws SgCryptoException, SgDataValidationException {
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
        Long accountId = service.getAccountIdByRegistrationEmail(dto.getEmail());
        AccountDto accountDto = service.getAccountInfo(accountId);
        AuthToken authToken = new AuthToken(accountDto, TokenExpirationType.LONG_TOKEN, Instant.now());
        String token = security.encryptSecurityToken(authToken);
        mailService.sendEmailVerificationEmail(token, dto.getEmail());
        response.setHeader(CustomHttpHeaders.X_ACCOUNT_ID, accountId.toString());
        return result;
    }

    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_ADMIN_USER, method = RequestMethod.POST)
    public @ResponseBody
    OperationStatusDto signupAdmin(@Valid @RequestBody SignupDto dto, HttpServletResponse response) throws IOException, SgCryptoException, SgDataValidationException {
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
        Long accountId = service.getAccountIdByRegistrationEmail(dto.getEmail());
        AccountDto accountDto = service.getAccountInfo(accountId);
        AuthToken authToken = new AuthToken(accountDto, TokenExpirationType.LONG_TOKEN, Instant.now());
        String token = security.encryptSecurityToken(authToken);
        mailService.sendEmailVerificationEmail(token, dto.getEmail());
        response.setHeader(CustomHttpHeaders.X_ACCOUNT_ID, accountId.toString());
        return result;
    }

    @RequestMapping(value = RequestPath.REQUEST_COMPLETE_SIGNUP, method = RequestMethod.POST)
    public @ResponseBody
    OperationStatusDto completeSignup(@Valid @RequestBody CompleteSignupDto dto) throws SgDataValidationException {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        OperationStatusDto attemptResult = new OperationStatusDto();

        try {
            service.completeSignup(userId, dto);
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
    public @ResponseBody
    OperationStatusDto signin(@Valid @RequestBody SigninDto dto, HttpServletResponse response) throws IOException, SgCryptoException, SgDataValidationException {
        OperationStatusDto result = new OperationStatusDto();
        result.setStatus(SigninStatus.STATUS_SUCCESS);

        try {
            service.signIn(dto);
            Long accountId = service.getAccountIdByRegistrationEmail(dto.getEmail());
            AccountDto accountDto = service.getAccountInfo(accountId);
            AuthToken authToken = new AuthToken(accountDto, TokenExpirationType.USER_SESSION_TOKEN, Instant.now());
            String token = security.encryptSecurityToken(authToken);
            response.setHeader(CustomHttpHeaders.X_AUTH_TOKEN, token);
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
