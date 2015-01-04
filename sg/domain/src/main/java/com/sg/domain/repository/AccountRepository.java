/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.repository;

import com.sg.domain.entites.Account;

/**
 *
 * @author Администратор
 */
public interface AccountRepository {
    public Account findByEmail(String email);
    public Account findOne(Long accountId);
    public void save(Account account);
    public void delete(Account account);
}
