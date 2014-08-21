/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.constants.Roles;
import com.sg.domain.service.exception.SgServiceLayerRuntimeException;
import com.sg.dto.CanvasDto;
import com.sg.dto.CanvasRefDto;
import com.sg.dto.CanvasUpdateDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.dto.response.AccountDto;
import com.sg.domain.entities.jpa.Canvas;
import com.sg.domain.entities.jpa.CanvasesRepository;
import com.sg.domain.entities.jpa.ProductRepository;
import com.sg.domain.entities.jpa.ThreadsRepository;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.transaction.support.TransactionTemplate;
import com.sg.domain.entities.jpa.Thread;
import com.sg.domain.entities.jpa.Account;
import com.sg.domain.entities.jpa.AccountsRepository;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.service.exception.SgCanvasNotFoundException;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.response.ThreadsListDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 *
 * @author tarasev
 */
//@Transactional annotation prevent spring context to be initialized on GAE
@Service
public class JpaServiceImpl implements SgService {

    @Resource(name = "productRepository")
    private ProductRepository productRepository;

    @Resource(name = "threadsRepository")
    private ThreadsRepository threadsRepository;

    @Resource(name = "canvasesRepository")
    private CanvasesRepository canvasesRepository;

    @Resource(name = "accountsRepository")
    private AccountsRepository accountsRepository;

    @Resource(name = "mapper")
    private MapperFacade mapper;

    @Resource(name = "transactionTemplate")
    private TransactionTemplate transactionTemplate;

    @Resource
    private ValidatorComponent validatorComponent;

    public void delete(final ThreadDeleteDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    deleteThreadImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void deleteThreadImpl(ThreadDeleteDto dto) {
        Thread thread = threadsRepository.findByCode(dto.getCode());
        if (thread == null) {
            throw new SgThreadNotFoundException(dto.getCode());
        }
        threadsRepository.delete(thread.getId());
    }

    public void create(final ThreadCreateDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    createThreadImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void createThreadImpl(ThreadCreateDto dto) {
        if (threadsRepository.findByCode(dto.getCode()) != null) {
            throw new SgThreadAlreadyExistsException(dto.getCode());
        }

        Thread thread = mapper.map(dto, Thread.class);
        threadsRepository.save(thread);
    }

    public ThreadsListDto listThreads() {
        return transactionTemplate.execute(new TransactionCallback<ThreadsListDto>() {
            public ThreadsListDto doInTransaction(TransactionStatus status) {
                try {
                    return listThreadsImpl();
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private ThreadsListDto listThreadsImpl() {
        ThreadsListDto result = new ThreadsListDto();
        Iterable<Thread> threads = threadsRepository.findAll();
        
        List<ThreadsListDto.ThreadInfo> list = new ArrayList<ThreadsListDto.ThreadInfo>();
        for (Thread t : threads) {
            list.add(mapper.map(t, ThreadsListDto.ThreadInfo.class));
        }
        
        result.setThreads(list);
        return result;
    }

    public void update(final ThreadUpdateDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    updateThreadImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void updateThreadImpl(ThreadUpdateDto dto) {
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

    public void create(final CanvasDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    createCanvasImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void createCanvasImpl(CanvasDto dto) {
        if (canvasesRepository.findByCode(dto.getCode()) != null) {
            throw new SgCanvasAlreadyExistsException(dto.getCode());
        }
        Canvas canvas = mapper.map(dto, Canvas.class);
        canvasesRepository.save(canvas);
    }

    public void delete(final CanvasRefDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    deleteCanvasImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void deleteCanvasImpl(CanvasRefDto dto) {
        Canvas canvas = canvasesRepository.findByCode(dto.getCode());
        if (canvas == null) {
            throw new SgCanvasNotFoundException(dto.getCode());
        }
        canvasesRepository.delete(canvas);
    }

    public void update(final CanvasUpdateDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    updateCanvasImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private void updateCanvasImpl(CanvasUpdateDto dto) {
        Canvas canvas = canvasesRepository.findByCode(dto.getRef().getCode());
        if (canvas == null) {
            throw new SgCanvasNotFoundException(dto.getRef().getCode());
        }
        if (canvasesRepository.findByCode(dto.getDto().getCode()) != null) {
            throw new SgCanvasAlreadyExistsException(dto.getDto().getCode());
        }
        mapper.map(dto.getDto(), canvas);
        canvasesRepository.save(canvas);
    }

    public List<CanvasDto> listCanvases() {
        return transactionTemplate.execute(new TransactionCallback<List<CanvasDto>>() {
            public List<CanvasDto> doInTransaction(TransactionStatus status) {
                try {
                    return listCanvasesImpl();
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    private List<CanvasDto> listCanvasesImpl() {
        List<CanvasDto> result = new ArrayList<CanvasDto>();
        Iterable<Canvas> canvases = canvasesRepository.findAll();
        for (Canvas c : canvases) {
            result.add(mapper.map(c, CanvasDto.class));
        }
        return result;
    }
    
    public void signupUser(final SignupDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        signup(dto, Roles.ROLE_USER);
    }

    public void signupAdmin(final SignupDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        signup(dto, Roles.ROLE_ADMIN, Roles.ROLE_USER);
    }

    private void signup(final SignupDto dto, final String... roles) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    signupImpl(dto, roles);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    public void signupImpl(SignupDto dto, String... roles) {
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

    public Long getAccountIdByRegistrationEmail(final String email) {
        return transactionTemplate.execute(new TransactionCallback<Long>() {
            public Long doInTransaction(TransactionStatus status) {
                try {
                    return getAccountIdByRegistrationEmailImpl(email);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    public Long getAccountIdByRegistrationEmailImpl(String email) {
        Account account = accountsRepository.findByEmail(email);
        if (account == null) {
            throw new SgAccountNotFoundException(email);
        }
        return account.getId();
    }

    public AccountDto getAccountInfo(final Long accountId) {
        return transactionTemplate.execute(new TransactionCallback<AccountDto>() {
            public AccountDto doInTransaction(TransactionStatus status) {
                try {
                    return getAccountInfoImpl(accountId);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    public AccountDto getAccountInfoImpl(Long accountId) {
        Account account = accountsRepository.findOne(accountId);
        if (account == null) {
            throw new SgAccountNotFoundException(accountId);
        }
        return mapper.map(account, AccountDto.class);
    }

    public void completeSignup(final Long userId, final CompleteSignupDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    completeSignupImpl(userId, dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    public void completeSignupImpl(Long accountId, CompleteSignupDto dto) {
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

    public void signIn(final SigninDto dto) throws SgDataValidationException {
        validatorComponent.validate(dto);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    signInImpl(dto);
                } catch (SgServiceLayerRuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SgServiceLayerRuntimeException(e);
                }
            }
        });
    }

    public void signInImpl(SigninDto dto) {
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

    public void ping() {
    }
}
