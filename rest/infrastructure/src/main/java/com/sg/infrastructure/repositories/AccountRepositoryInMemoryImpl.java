/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.infrastructure.repositories;

import com.sg.domain.ar.Account;
import com.sg.domain.repositories.AccountRepository;
import com.sg.domain.vo.AccountId;
import com.sg.domain.vo.Email;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public class AccountRepositoryInMemoryImpl implements AccountRepository {

    private final List<Account> accounts = new LinkedList<Account>();

    @Override
    public Account getAccountByAccountId(AccountId accountId) {
        for (Account a : accounts) {
            if (a.getAccountId().equals(accountId)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public Account getAccountByEmail(Email email) {
        for (Account a : accounts) {
            if (a.getEmailAccount() != null) {
                if (a.getEmailAccount().getEmail().equals(email)) {
                    return a;
                }
            }
        }
        return null;
    }

    @Override
    public void create(Account account) {
        accounts.add(account);
    }

    @Override
    public void update(Account account) {
        Iterator<Account> iterator = accounts.iterator();
        while (iterator.hasNext()) {
            Account a = iterator.next();
            if (a.getAccountId().equals(account.getAccountId())) {
                iterator.remove();
                break;
            }
        }
        accounts.add(account);
    }

}
