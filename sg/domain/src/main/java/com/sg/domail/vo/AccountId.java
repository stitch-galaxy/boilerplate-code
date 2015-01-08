/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

/**
 *
 * @author tarasev
 */
public class AccountId {

    private final Long id;

    public AccountId(Long id) {
        this.id = id;
        verifyData();
    }
    
    private void verifyData()
    {
        if (id == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
}
