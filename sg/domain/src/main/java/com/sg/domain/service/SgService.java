/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgAccountWithoutEmailException;
import com.sg.domain.service.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.service.exception.SgCanvasNotFoundException;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInstallationAlreadyCompletedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.dto.request.CanvasCreateDto;
import com.sg.dto.request.CanvasDeleteDto;
import com.sg.dto.request.CanvasUpdateDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.ResetPasswordDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.UserInfoUpdateDto;
import com.sg.dto.response.AccountRolesDto;
import com.sg.dto.response.CanvasesListDto;
import com.sg.dto.response.ThreadsListDto;
import com.sg.dto.response.UserInfoDto;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author tarasev
 */
public interface SgService {
    
    public void create(@Valid ThreadCreateDto dto) throws SgDataValidationException, SgThreadAlreadyExistsException;
    public void delete(@Valid ThreadDeleteDto dto) throws SgDataValidationException, SgThreadNotFoundException;
    public void update(@Valid ThreadUpdateDto dto) throws SgDataValidationException, SgThreadNotFoundException, SgThreadAlreadyExistsException;
    public @Valid ThreadsListDto listThreads();   
    
    public void create(@Valid CanvasCreateDto dto) throws SgDataValidationException, SgCanvasAlreadyExistsException;
    public void delete(@Valid CanvasDeleteDto dto) throws SgDataValidationException, SgCanvasNotFoundException;
    public void update(@Valid CanvasUpdateDto dto) throws SgDataValidationException, SgCanvasNotFoundException, SgCanvasAlreadyExistsException;
    public @Valid CanvasesListDto listCanvases();   
   
    public void install() throws SgInstallationAlreadyCompletedException;
    
    public void signupUser(@Valid SignupDto dto) throws SgDataValidationException, SgSignupForRegisteredButNonVerifiedEmailException, SgSignupAlreadyCompletedException;
    public void signupAdmin(@Valid SignupDto dto) throws SgDataValidationException, SgSignupForRegisteredButNonVerifiedEmailException, SgSignupAlreadyCompletedException;
    
    public @NotNull Long getAccountId(@Email String email) throws SgDataValidationException, SgAccountNotFoundException;
    
    public @Valid AccountRolesDto getAccountRoles(@NotNull Long accountId) throws SgAccountNotFoundException;
    
    public void completeSignup(@NotNull Long accountId, @Valid CompleteSignupDto dto) throws SgDataValidationException, SgAccountNotFoundException, SgSignupAlreadyCompletedException;
    
    public void signIn(@Valid SigninDto dto) throws SgDataValidationException, SgAccountNotFoundException, SgInvalidPasswordException, SgEmailNonVerifiedException;
    
    public void ping() throws Exception;
    
    public void setUserInfo(@NotNull Long accountId, @Valid UserInfoUpdateDto dto)  throws SgDataValidationException, SgAccountNotFoundException;
    public @Valid UserInfoDto getUserInfo(@NotNull Long accountId)  throws SgAccountNotFoundException;
    
    public void deleteAccount(@NotNull Long accountId)  throws SgAccountNotFoundException;
    public void resetPassword(@NotNull Long accountId, @Valid ResetPasswordDto dto) throws SgDataValidationException, SgAccountNotFoundException, SgAccountWithoutEmailException, SgEmailNonVerifiedException;
}
