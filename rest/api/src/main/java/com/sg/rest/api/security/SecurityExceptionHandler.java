/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api.security;

import com.sg.rest.api.dto.TokenAuthentificationFailed;
import com.sg.rest.api.security.exceptions.AppSecurityAccountNotFoundException;
import com.sg.rest.api.security.exceptions.AppSecurityBadTokenException;
import com.sg.rest.api.security.exceptions.AppSecurityNoTokenException;
import com.sg.rest.api.security.exceptions.AppSecurityTokenExpiredException;
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

    @ExceptionHandler(AppSecurityBadTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processAppSecurityBadTokenException(Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.BAD_TOKEN);
    }

    @ExceptionHandler(AppSecurityTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processAppSecurityTokenExpiredException(Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.TOKEN_EXPIRED);
    }

    @ExceptionHandler(AppSecurityNoTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processAppSecurityNoTokenException(Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.NO_TOKEN);
    }

    @ExceptionHandler(AppSecurityAccountNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenAuthentificationFailed processAppSecurityAccountNotFoundException(Exception ex) throws Exception {
        return new TokenAuthentificationFailed(TokenAuthentificationFailed.Reason.ACCOUNT_NOT_FOUND);
    }

}
