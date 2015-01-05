/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service.jpa.components;

import com.sg.domain.enumerations.Roles;
import com.sg.dto.request.CanvasCreateDto;
import com.sg.dto.request.CanvasDeleteDto;
import com.sg.dto.request.CanvasUpdateDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.domain.entities.jpa.Canvas;
import com.sg.domain.jpa.repository.CanvasRepository;
import com.sg.domain.jpa.repository.ProductRepository;
import com.sg.domain.jpa.repository.ThreadRepository;
import java.util.List;
import com.sg.domain.entities.jpa.Thread;
import com.sg.domain.entities.jpa.Account;
import com.sg.domain.jpa.repository.AccountRepository;
import com.sg.domain.service.SgService;
import com.sg.domain.exception.SgAccountNotFoundException;
import com.sg.domain.exception.SgAccountWithoutEmailException;
import com.sg.domain.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.exception.SgCanvasNotFoundException;
import com.sg.domain.exception.SgEmailNonVerifiedException;
import com.sg.domain.exception.SgInvalidPasswordException;
import com.sg.domain.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.exception.SgThreadAlreadyExistsException;
import com.sg.domain.exception.SgThreadNotFoundException;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.ResetPasswordDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.UserInfoUpdateDto;
import com.sg.dto.response.AccountRolesDto;
import com.sg.dto.response.CanvasesListDto;
import com.sg.dto.response.ThreadsListDto;
import com.sg.dto.response.UserInfoDto;
import java.util.ArrayList;
import java.util.Arrays;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author tarasev
 */
@Transactional
@Service
@Validated
public class SgServiceImpl implements SgService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ThreadRepository threadsRepository;

    @Autowired
    private CanvasRepository canvasesRepository;

    @Autowired
    private AccountRepository accountsRepository;

    @Autowired
    private MapperFacade mapper;

    @Value("${admin.email}")
    private String ADMIN_EMAIL;
    @Value("${admin.password}")
    private String ADMIN_PASSWORD;
    @Value("${user.email}")
    private String USER_EMAIL;
    @Value("${user.password}")
    private String USER_PASSWORD;
    

    @Override
    public void delete(ThreadDeleteDto dto) {
        Thread thread = threadsRepository.findByCode(dto.getCode());
        if (thread == null) {
            throw new SgThreadNotFoundException(dto.getCode());
        }
        threadsRepository.delete(thread.getId());
    }

    @Override
    public void create(ThreadCreateDto dto) {
        if (threadsRepository.findByCode(dto.getCode()) != null) {
            throw new SgThreadAlreadyExistsException(dto.getCode());
        }

        Thread thread = mapper.map(dto, Thread.class);
        threadsRepository.save(thread);
    }

    @Override
    public ThreadsListDto listThreads() {
        ThreadsListDto result = new ThreadsListDto();
        Iterable<Thread> threads = threadsRepository.findAll();

        List<ThreadsListDto.ThreadInfo> list = new ArrayList<ThreadsListDto.ThreadInfo>();
        for (Thread t : threads) {
            list.add(mapper.map(t, ThreadsListDto.ThreadInfo.class));
        }

        result.setThreads(list);
        return result;
    }

    @Override
    public void update(ThreadUpdateDto dto) {
        Thread thread = threadsRepository.findByCode(dto.getRefCode());
        if (thread == null) {
            throw new SgThreadNotFoundException(dto.getRefCode());
        }
        if (threadsRepository.findByCode(dto.getCode()) != null) {
            throw new SgThreadAlreadyExistsException(dto.getCode());
        }

        mapper.map(dto, thread);
        threadsRepository.save(thread);
    }

    @Override
    public void create(CanvasCreateDto dto) {
        if (canvasesRepository.findByCode(dto.getCode()) != null) {
            throw new SgCanvasAlreadyExistsException(dto.getCode());
        }
        Canvas canvas = mapper.map(dto, Canvas.class);
        canvasesRepository.save(canvas);
    }

    @Override
    public void delete(CanvasDeleteDto dto) {
        Canvas canvas = canvasesRepository.findByCode(dto.getCode());
        if (canvas == null) {
            throw new SgCanvasNotFoundException(dto.getCode());
        }
        canvasesRepository.delete(canvas);
    }

    @Override
    public void update(CanvasUpdateDto dto) {
        Canvas canvas = canvasesRepository.findByCode(dto.getRefCode());
        if (canvas == null) {
            throw new SgCanvasNotFoundException(dto.getRefCode());
        }
        if (canvasesRepository.findByCode(dto.getCode()) != null) {
            throw new SgCanvasAlreadyExistsException(dto.getCode());
        }
        mapper.map(dto, canvas);
        canvasesRepository.save(canvas);
    }

    @Override
    public CanvasesListDto listCanvases() {
        CanvasesListDto result = new CanvasesListDto();
        Iterable<Canvas> canvases = canvasesRepository.findAll();

        List<CanvasesListDto.CanvasInfo> list = new ArrayList<CanvasesListDto.CanvasInfo>();
        for (Canvas c : canvases) {
            list.add(mapper.map(c, CanvasesListDto.CanvasInfo.class));
        }

        result.setCanvases(list);
        return result;
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
        Account account = accountsRepository.findByEmail(dto.getEmail());
        if (account != null) {
            if (account.getEmailVerified() == Boolean.FALSE) {
                throw new SgSignupForRegisteredButNonVerifiedEmailException(dto.getEmail());
            } else {
                throw new SgSignupAlreadyCompletedException(dto.getEmail());
            }
        }
        account = mapper.map(dto, Account.class);
        account.setEmailVerified(Boolean.FALSE);
        account.setRoles(Arrays.asList(roles));
        accountsRepository.save(account);
    }

    @Override
    public void completeSignup(Long accountId, CompleteSignupDto dto) {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        if (account.getEmailVerified() == Boolean.TRUE) {
            throw new SgSignupAlreadyCompletedException(account.getEmail());
        }
        account.setEmailVerified(Boolean.TRUE);
        account.setPassword(dto.getPassword());
        accountsRepository.save(account);
    }

    @Override
    public void signIn(SigninDto dto) {
        Account account = accountsRepository.findByEmail(dto.getEmail());
        if (account == null) {
            throw new SgAccountNotFoundException(dto.getEmail());
        }
        if (account.getEmailVerified() == Boolean.FALSE) {
            throw new SgEmailNonVerifiedException(dto.getEmail());
        }
        if (!dto.getPassword().equals(account.getPassword())) {
            throw new SgInvalidPasswordException();
        }
    }

    @Override
    public void setUserInfo(Long accountId, UserInfoUpdateDto dto) throws SgAccountNotFoundException {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        mapper.map(dto, account);
        accountsRepository.save(account);
    }

    @Override
    public UserInfoDto getUserInfo(final Long accountId) throws SgAccountNotFoundException {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        return mapper.map(account, UserInfoDto.class);
    }

    @Override
    public void deleteAccount(Long accountId) throws SgAccountNotFoundException {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        accountsRepository.delete(account);
    }

    @Override
    public void resetPassword(Long accountId, ResetPasswordDto dto) throws SgAccountNotFoundException, SgAccountWithoutEmailException {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        if (account.getEmail() == null) {
            throw new SgAccountWithoutEmailException(accountId);
        }
        if (account.getEmailVerified() == Boolean.FALSE) {
            throw new SgEmailNonVerifiedException((account.getEmail()));
        }
        mapper.map(dto, account);
        accountsRepository.save(account);
    }

    @Override
    public Long getAccountId(String email) {
        Account account = accountsRepository.findByEmail(email);
        if (account == null) {
            throw new SgAccountNotFoundException(email);
        }
        return account.getId();
    }

    @Override
    public AccountRolesDto getAccountRoles(Long accountId) throws SgAccountNotFoundException {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        return mapper.map(account, AccountRolesDto.class);
    }
    
    @Override
    public void ping()
    {
    }
}
