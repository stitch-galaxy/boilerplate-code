/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.vo;

import java.util.UUID;

/**
 *
 * @author Admin
 */
public class AccountId extends UuidId {

    public static AccountId create()
    {
        return new AccountId(UUID.randomUUID());
    }
    
    public AccountId(UUID id) {
        super(id);
    }
}
