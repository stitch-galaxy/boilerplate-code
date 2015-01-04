/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import com.sg.domain.exception.SgAccountNotFoundException;
import com.sg.domain.exception.SgAccountWithoutEmailException;
import com.sg.domain.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.exception.SgCanvasNotFoundException;
import com.sg.domain.exception.SgEmailNonVerifiedException;
import com.sg.domain.exception.SgInstallationAlreadyCompletedException;
import com.sg.domain.exception.SgInvalidPasswordException;
import com.sg.domain.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.exception.SgThreadAlreadyExistsException;
import com.sg.domain.exception.SgThreadNotFoundException;
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

/**
 *
 * @author tarasev
 */
public interface SgService {
    
    public void create(ThreadCreateDto dto) throws SgThreadAlreadyExistsException;
    public void delete(ThreadDeleteDto dto) throws SgThreadNotFoundException;
    public void update(ThreadUpdateDto dto) throws SgThreadNotFoundException, SgThreadAlreadyExistsException;
    public ThreadsListDto listThreads();   
    
    public void create(CanvasCreateDto dto) throws SgCanvasAlreadyExistsException;
    public void delete(CanvasDeleteDto dto) throws SgCanvasNotFoundException;
    public void update(CanvasUpdateDto dto) throws SgCanvasNotFoundException, SgCanvasAlreadyExistsException;
    public CanvasesListDto listCanvases();   
   
    public void install() throws SgInstallationAlreadyCompletedException;
    
    public void signupUser(SignupDto dto) throws SgSignupForRegisteredButNonVerifiedEmailException, SgSignupAlreadyCompletedException;
    public void signupAdmin(SignupDto dto) throws SgSignupForRegisteredButNonVerifiedEmailException, SgSignupAlreadyCompletedException;
    
    public Long getAccountId(String email) throws SgAccountNotFoundException;
    
    public AccountRolesDto getAccountRoles(Long accountId) throws SgAccountNotFoundException;
    
    public void completeSignup(Long accountId, CompleteSignupDto dto) throws SgAccountNotFoundException, SgSignupAlreadyCompletedException;
    
    public void signIn(SigninDto dto) throws SgAccountNotFoundException, SgInvalidPasswordException, SgEmailNonVerifiedException;
    
    public void ping() throws Exception;
    
    public void setUserInfo(Long accountId, UserInfoUpdateDto dto)  throws SgAccountNotFoundException;
    public UserInfoDto getUserInfo(Long accountId)  throws SgAccountNotFoundException;
    
    public void deleteAccount(Long accountId)  throws SgAccountNotFoundException;
    public void resetPassword(Long accountId, ResetPasswordDto dto) throws SgAccountNotFoundException, SgAccountWithoutEmailException, SgEmailNonVerifiedException;
}
