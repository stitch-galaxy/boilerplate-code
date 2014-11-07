/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.repositories;

import com.sg.domain.entities.jpa.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tarasev
 */
@Repository
public interface AccountsRepository extends CrudRepository<Account, Long> {
    Account findByEmail(String email);
}
