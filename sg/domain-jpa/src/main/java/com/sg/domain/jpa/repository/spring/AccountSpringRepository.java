/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.jpa.repository.spring;

import com.sg.domain.jpa.entities.AccountProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tarasev
 */
@Repository
public interface AccountSpringRepository extends CrudRepository<AccountProjection, Long> {
    public AccountProjection findByEmail(String email);
}
