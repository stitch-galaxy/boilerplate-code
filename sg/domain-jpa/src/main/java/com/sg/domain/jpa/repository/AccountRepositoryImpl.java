/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.jpa.repository;

import com.sg.domain.ar.Account;
import com.sg.domain.jpa.entities.AccountProjection;
import com.sg.domain.jpa.repository.spring.AccountSpringRepository;
import com.sg.domain.repository.AccountRepository;

/**
 *
 * @author tarasev
 */
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountSpringRepository springRepository;
    
    public AccountRepositoryImpl(AccountSpringRepository springRepository)
    {
        this.springRepository = springRepository;
    }
    
    @Override
    public Account findOne(long accountId) {
        AccountProjection projection = springRepository.findOne(accountId);
        //TODO: restore
        return null;
    }

    @Override
    public Account findByEmail(String email) {
        AccountProjection projection = springRepository.findByEmail(email);
        //TODO: restore
        return null;
    }

    @Override
    public void save(Account account) {
        //TODO: project
        AccountProjection projection = null;
        springRepository.save(projection);
    }
    
}
