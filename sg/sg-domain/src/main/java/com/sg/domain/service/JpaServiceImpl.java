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
import com.sg.dto.ThreadDto;
import com.sg.dto.ThreadRefDto;
import com.sg.dto.ThreadUpdateDto;
import com.sg.dto.AccountDto;
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
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgEmailAlreadySignedUpCompletellyException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.dto.CompleteSignupDto;
import com.sg.dto.SigninDto;
import com.sg.dto.SignupDto;
import java.util.ArrayList;
import java.util.Arrays;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.validation.Validator;

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
    private Validator validator;

    public void delete(final ThreadRefDto dto) {
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

    private void deleteThreadImpl(ThreadRefDto dto) {
        Thread thread = threadsRepository.findByCode(dto.getCode());
        if (thread == null) {
            throw new SgThreadNotFoundException(dto.getCode());
        }
        threadsRepository.delete(thread.getId());
    }

    public void create(final ThreadDto dto) {
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

    private void createThreadImpl(ThreadDto dto) {
        if (threadsRepository.findByCode(dto.getCode()) != null) {
            throw new SgThreadAlreadyExistsException(dto.getCode());
        }

        Thread thread = mapper.map(dto, Thread.class);
        threadsRepository.save(thread);
    }

    public List<ThreadDto> listThreads() {
        return transactionTemplate.execute(new TransactionCallback<List<ThreadDto>>() {
            public List<ThreadDto> doInTransaction(TransactionStatus status) {
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

    private List<ThreadDto> listThreadsImpl() {
        List<ThreadDto> result = new ArrayList<ThreadDto>();
        Iterable<Thread> threads = threadsRepository.findAll();
        for (Thread t : threads) {
            result.add(mapper.map(t, ThreadDto.class));
        }
        return result;
    }

    public void update(final ThreadUpdateDto dto) {
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
        Thread thread = threadsRepository.findByCode(dto.getRef().getCode());
        if (thread == null) {
            throw new SgThreadNotFoundException(dto.getRef().getCode());
        }
        if (dto.getRef().getCode().equals(dto.getDto().getCode())) {
            return;
        }
        if (threadsRepository.findByCode(dto.getDto().getCode()) != null) {
            throw new SgThreadAlreadyExistsException(dto.getDto().getCode());
        }

        mapper.map(dto.getDto(), thread);
        threadsRepository.save(thread);
    }

    public void create(final CanvasDto dto) {
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

    public void delete(final CanvasRefDto dto) {
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

    public void update(final CanvasUpdateDto dto) {
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
        if (dto.getRef().getCode().equals(dto.getDto().getCode())) {
            return;
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

    public void signupUser(final SignupDto dto) {
        signup(dto, Roles.ROLE_USER);
    }

    public void signupAdmin(final SignupDto dto) {
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
            if (account.getEmailVerified() == Boolean.FALSE)
            {
                throw new SgSignupForRegisteredButNonVerifiedEmailException(dto.getEmail());
            }
            else
            {
                throw new SgEmailAlreadySignedUpCompletellyException(dto.getEmail());
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
    
    public Long getAccountIdByRegistrationEmailImpl(String email)
    {
        Account account = accountsRepository.findByEmail(email);
        if (account == null)
            throw new SgAccountNotFoundException(email);
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
        if (account == null)
            throw new SgAccountNotFoundException(accountId);
        return mapper.map(account, AccountDto.class);
    }

    public void completeSignup(final Long userId, final CompleteSignupDto dto) {
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
            throw new SgSignupAlreadyCompletedException();
        }
        account.setEmailVerified(Boolean.TRUE);
        account.setPassword(dto.getPassword());
        accountsRepository.save(account);
    }
    
    public void signIn(final SigninDto dto) {
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
        if (account.getEmailVerified() == Boolean.FALSE)
        {
            throw new SgEmailNonVerifiedException(dto.getEmail());
        }
        if (!dto.getPassword().equals(account.getPassword())) {
            throw new SgInvalidPasswordException();
        }
    }
}
