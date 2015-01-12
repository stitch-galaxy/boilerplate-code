/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.domain.entites.Account;
import com.sg.domain.enumerations.Roles;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.ResetPasswordDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.UserInfoUpdateDto;
import com.sg.dto.response.AccountRolesDto;
import com.sg.dto.response.UserInfoDto;

import com.sg.domain.repository.CanvasRepository;
import com.sg.domain.repository.ProductRepository;
import com.sg.domain.repository.ThreadRepository;
import com.sg.domain.repository.AccountRepository;

import com.sg.domain.exception.SgAccountNotFoundException;
import com.sg.domain.exception.SgAccountWithoutEmailException;
import com.sg.domain.exception.SgEmailNonVerifiedException;
import com.sg.domain.exception.SgInvalidPasswordException;
import com.sg.domain.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import java.util.Arrays;

/**
 *
 * @author tarasev
 */
public class SgServiceImpl implements SgService {

    private final ProductRepository productRepository;
    private final ThreadRepository threadRepository;
    private final CanvasRepository canvasRepository;
    private final AccountRepository accountRepository;

    private final DtoDoMapper mapper;

    public SgServiceImpl(ProductRepository productRepository,
            ThreadRepository threadRepository,
            CanvasRepository canvasRepository,
            AccountRepository accountRepository) {
        this.productRepository = productRepository;
        this.threadRepository = threadRepository;
        this.canvasRepository = canvasRepository;
        this.accountRepository = accountRepository;
        this.mapper = new DtoToDoOrikaMapper();
    }

    @Override
    public void signupUser(final SignupDto dto) {
        signup(dto, Roles.USER);
    }

    @Override
    public void signupAdmin(final SignupDto dto) {
        signup(dto, Roles.ADMIN, Roles.USER);
    }

    private void signup(SignupDto dto, String... roles) {
        Account account = accountRepository.findByEmail(dto.getEmail());
        if (account != null) {
            if (account.getEmailVerified().equals(Boolean.FALSE)) {
                throw new SgSignupForRegisteredButNonVerifiedEmailException(dto.getEmail());
            } else {
                throw new SgSignupAlreadyCompletedException(dto.getEmail());
            }
        }
        account = mapper.map(dto, Account.class);
        account.setEmailVerified(Boolean.FALSE);
        account.setRoles(Arrays.asList(roles));
        accountRepository.save(account);
    }

    @Override
    public void completeSignup(Long accountId, CompleteSignupDto dto) {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        if (account.getEmailVerified().equals(Boolean.TRUE)) {
            throw new SgSignupAlreadyCompletedException(account.getEmail());
        }
        account.setEmailVerified(Boolean.TRUE);
        account.setPassword(dto.getPassword());
        accountRepository.save(account);
    }

    @Override
    public void signIn(SigninDto dto) {
        Account account = accountRepository.findByEmail(dto.getEmail());
        if (account == null) {
            throw new SgAccountNotFoundException(dto.getEmail());
        }
        if (account.getEmailVerified().equals(Boolean.FALSE)) {
            throw new SgEmailNonVerifiedException(dto.getEmail());
        }
        if (!dto.getPassword().equals(account.getPassword())) {
            throw new SgInvalidPasswordException();
        }
    }

    @Override
    public void setUserInfo(Long accountId, UserInfoUpdateDto dto) throws SgAccountNotFoundException {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        mapper.map(dto, account);
        accountRepository.save(account);
    }

    @Override
    public UserInfoDto getUserInfo(final Long accountId) throws SgAccountNotFoundException {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        return mapper.map(account, UserInfoDto.class);
    }

    @Override
    public void deleteAccount(Long accountId) throws SgAccountNotFoundException {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        accountRepository.delete(account);
    }

    @Override
    public void resetPassword(Long accountId, ResetPasswordDto dto) throws SgAccountNotFoundException, SgAccountWithoutEmailException {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        if (account.getEmail() == null) {
            throw new SgAccountWithoutEmailException(accountId);
        }
        if (account.getEmailVerified().equals(Boolean.FALSE)) {
            throw new SgEmailNonVerifiedException((account.getEmail()));
        }
        mapper.map(dto, account);
        accountRepository.save(account);
    }

    @Override
    public Long getAccountId(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new SgAccountNotFoundException(email);
        }
        return account.getId();
    }

    @Override
    public AccountRolesDto getAccountRoles(Long accountId) throws SgAccountNotFoundException {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        return mapper.map(account, AccountRolesDto.class);
    }

    @Override
    public void ping() {
    }
}
