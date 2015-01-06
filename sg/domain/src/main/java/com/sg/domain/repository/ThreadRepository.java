/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.repository;

import com.sg.domain.entites.Thread;
/**
 *
 * @author Администратор
 */
public interface ThreadRepository {
    public Thread findByCode(String code);
    public Iterable<Thread> findAll();
    public void delete(Thread thread);
    public void save(Thread thread);
}
