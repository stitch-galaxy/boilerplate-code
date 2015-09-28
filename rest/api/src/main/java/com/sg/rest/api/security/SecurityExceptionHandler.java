/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api.security;

import com.sg.domain.exceptions.TokenBasedSecurityAccountNotFoundException;
import com.sg.domain.exceptions.TokenBasedSecurityBadTokenException;
import com.sg.domain.exceptions.TokenBasedSecurityNoTokenException;
import com.sg.domain.exceptions.TokenBasedSecurityNotAcceptableTokenTypeException;
import com.sg.domain.exceptions.TokenBasedSecurityTokenExpiredException;
import com.sg.domain.exceptions.TokenBasedSecurityTokenRevokedException;
import com.sg.domain.exceptions.TokenBasedSecurityTokenTypeNotAvailiableException;
import com.sg.rest.api.dto.TokenAuthentificationFailed;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Admin
 */
@ControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(TokenBasedSecurityBadTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processTokenBasedSecurityBadTokenException(
            Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.BAD_TOKEN);
    }

    @ExceptionHandler(TokenBasedSecurityTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processTokenBasedSecurityTokenExpiredException(
            Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.TOKEN_EXPIRED);
    }

    @ExceptionHandler(TokenBasedSecurityNoTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processTokenBasedSecurityNoTokenException(
            Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.NO_TOKEN);
    }

    @ExceptionHandler(TokenBasedSecurityAccountNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processTokenBasedSecurityAccountNotFoundException(
            Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.ACCOUNT_NOT_FOUND);
    }

    @ExceptionHandler(TokenBasedSecurityNotAcceptableTokenTypeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processTokenBasedSecurityNotAcceptableTokenTypeException(
            Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.NOT_ACCEPTABLE_TOKEN_TYPE);
    }

    @ExceptionHandler(TokenBasedSecurityTokenRevokedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processTokenBasedSecurityTokenRevokedException(
            Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.TOKEN_REVOKED);
    }

    @ExceptionHandler(TokenBasedSecurityTokenTypeNotAvailiableException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processTokenBasedSecurityTokenTypeNotAvailiableException(
            Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.TOKEN_TYPE_NOT_AVAILIABLE);
    }

}
