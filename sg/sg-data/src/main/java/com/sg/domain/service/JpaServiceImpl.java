/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.domain.dto.ThreadDto;
import com.sg.domain.entities.jpa.ProductRepository;
import com.sg.domain.entities.jpa.ThreadsRepository;
import java.util.List;
import javax.annotation.Resource;
import org.dozer.Mapper;
import org.springframework.transaction.support.TransactionTemplate;
import com.sg.domain.entities.jpa.Thread;
import java.util.ArrayList;
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

    public void addThread(final ThreadDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    addThreadImpl(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void addThreadImpl(ThreadDto dto) {
        Thread thread = mapper.map(dto, Thread.class);
        threadsRepository.save(thread);
    }

    public List<ThreadDto> getAllThreads() {
        return transactionTemplate.execute(new TransactionCallback<List<ThreadDto>>() {
            public List<ThreadDto> doInTransaction(TransactionStatus status) {
                try {
                    return getAllThreadsImpl();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private List<ThreadDto> getAllThreadsImpl() {
        List<ThreadDto> result = new ArrayList<ThreadDto>();
        Iterable<Thread> threads = threadsRepository.findAll();
        for (Thread t : threads) {
            result.add(mapper.map(t, ThreadDto.class));
        }
        return result;
    }

//    public void addProductSimple() {
//        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
//            protected void doInTransactionWithoutResult(TransactionStatus status) {
//                try {
//                    addProductImpl();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }
//    
//    public Long addProduct() {
//        return transactionTemplate.execute(new TransactionCallback<Long>() {
//            public Long doInTransaction(TransactionStatus status) {
//                try {
//                    return addProductImpl();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                
//            }
//        });
//    }
//
//    public Long addProductImpl() {
//        Product p = new Product();
//        p.setBlocked(Boolean.TRUE);
//        p.setPrice(BigDecimal.ZERO);
//        p.setDate(new LocalDate(2014, 1, 1));
//        productRepository.save(p);
//        
//        return p.getId();
//    }
}
