/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.entities.jpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tarasev
 */
@Repository
@Qualifier("threadsRepository")
public interface ThreadsRepository extends CrudRepository<Thread, Integer> {
    Thread findByCode(String code);
}
