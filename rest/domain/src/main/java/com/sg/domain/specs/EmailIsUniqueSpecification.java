/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.specs;

import com.sg.domain.repositories.AccountRepository;
import com.sg.domain.vo.Email;

/**
 *
 * @author Admin
 */
public class EmailIsUniqueSpecification {

    private final AccountRepository accountRepository;

    public EmailIsUniqueSpecification(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean isSatisfiedBy(Email email) {
        if (accountRepository.getAccountByEmail(email) != null) {
            return false;
        }
        return true;
    }
}
