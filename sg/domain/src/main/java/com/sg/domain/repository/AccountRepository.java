/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.repository;

import com.sg.domain.ar.Account;

/**
 *
 * @author tarasev
 */
public interface AccountRepository {
    public Account findOne(long accountId);
    public Account findByEmail(String email);
    public void save(Account account);
}
