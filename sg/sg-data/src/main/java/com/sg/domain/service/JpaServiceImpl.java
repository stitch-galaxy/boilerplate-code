/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.domain.dto.ThreadDto;
import com.sg.domain.dto.ThreadRefDto;
import com.sg.domain.dto.ThreadUpdateDto;
import com.sg.domain.entities.jpa.ProductRepository;
import com.sg.domain.entities.jpa.ThreadsRepository;
import java.util.List;
import javax.annotation.Resource;
import org.dozer.Mapper;
import org.springframework.transaction.support.TransactionTemplate;
import com.sg.domain.entities.jpa.Thread;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 *
 * @author tarasev
 */
//@Transactional annotation prevent spring context to be initialized on GAE
public class JpaServiceImpl implements SgService {

    @Resource(name = "productRepository")
    ProductRepository productRepository;

    @Resource(name = "threadsRepository")
    ThreadsRepository threadsRepository;

    @Resource(name = "mapper")
    Mapper mapper;

    @Resource(name = "transactionTemplate")
    private TransactionTemplate transactionTemplate;
    
    public void deleteThread(final ThreadRefDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    deleteThreadImpl(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    private void deleteThreadImpl(ThreadRefDto dto) {
        Thread thread = threadsRepository.findByCode(dto.getCode());
        threadsRepository.delete(thread.getId());
    }
    
    public void createThread(final ThreadDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    createThreadImpl(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void createThreadImpl(ThreadDto dto) {
        Thread thread = mapper.map(dto, Thread.class);
        threadsRepository.save(thread);
    }

    public List<ThreadDto> listThreads() {
        return transactionTemplate.execute(new TransactionCallback<List<ThreadDto>>() {
            public List<ThreadDto> doInTransaction(TransactionStatus status) {
                try {
                    return listThreadsImpl();
                } catch (Exception e) {
                    throw new RuntimeException(e);
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

    public void updateThread(final ThreadUpdateDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    updateThreadImpl(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    private void updateThreadImpl(ThreadUpdateDto dto) {
        Thread thread = threadsRepository.findByCode(dto.getThreadRef().getCode());
        mapper.map(dto.getThread(), thread);
        threadsRepository.save(thread);
    }
    
}
