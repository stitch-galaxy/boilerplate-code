/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.domain.entities.jpa.Product;
import com.sg.domain.entities.jpa.ProductRepository;
import java.math.BigDecimal;
import javax.annotation.Resource;
import org.joda.time.LocalDate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * @author tarasev
 */
public class StitchGalaxyService {

    @Resource(name = "productRepository")
    ProductRepository productRepository;

    //@Transactional annotation prevent spring context to be initialized on GAE
    @Resource(name = "transactionTemplate")
    private TransactionTemplate transactionTemplate;

    public void addProductSimple() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    addProductImpl();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    public Long addProduct() {
        return transactionTemplate.execute(new TransactionCallback<Long>() {
            public Long doInTransaction(TransactionStatus status) {
                try {
                    return addProductImpl();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                
            }
        });
    }

    public Long addProductImpl() {
        Product p = new Product();
        p.setBlocked(Boolean.TRUE);
        p.setPrice(BigDecimal.ZERO);
        p.setDate(new LocalDate(2014, 1, 1));
        productRepository.save(p);
        
        return p.getId();
    }
}
